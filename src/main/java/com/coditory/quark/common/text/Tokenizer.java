package com.coditory.quark.common.text;

import java.util.function.Consumer;

import static com.coditory.quark.common.check.Args.check;
import static com.coditory.quark.common.check.Args.checkNotNull;
import static com.coditory.quark.common.check.Asserts.assertThat;

public final class Tokenizer {
    private static final CodePointMatcher EMPTY_DELIMITERS = CodePointMatcher.none();
    private static final CodePointMatcher QUOTES = CodePointMatcher.anyOf('\"', '\'');
    private static final CodePointMatcher WHITESPACES = CodePointMatcher.whitespace();
    private static final CodePointMatcher ESCAPES = CodePointMatcher.anyOf('\\');

    private final CodePointMatcher quotes;
    private final CodePointMatcher delimiters;
    private final CodePointMatcher escapes;
    private final String text;
    private final Cursor cursor;

    public Tokenizer(String text) {
        this(text, EMPTY_DELIMITERS, EMPTY_DELIMITERS, ESCAPES);
    }

    private Tokenizer(String text, CodePointMatcher quotes, CodePointMatcher delimiters, CodePointMatcher escapes) {
        this.quotes = checkNotNull(quotes, "quotes");
        this.delimiters = checkNotNull(delimiters, "delimiters");
        this.text = checkNotNull(text, "text");
        this.cursor = Cursor.beginningOf(text);
        this.escapes = checkNotNull(escapes, "escapes");
    }

    public boolean atEnd() {
        return cursor.end();
    }

    public boolean atBeginning() {
        return cursor.beginning();
    }

    public int peekNextCodePoint() {
        check(!cursor.end(), "Cursor is at the end. There is no next character.");
        return text.codePointAt(cursor.getPosition());
    }

    public int nextCodePoint() {
        check(!cursor.end(), "Cursor is at the end. There is no next character.");
        int c = text.codePointAt(cursor.getPosition());
        cursor.increment(Character.charCount(c));
        return c;
    }

    public int peekNextChar() {
        check(!cursor.end(), "Cursor is at the end. There is no next character.");
        return text.charAt(cursor.getPosition());
    }

    public char nextChar() {
        check(!cursor.end(), "Cursor is at the end. There is no next character.");
        char c = text.charAt(cursor.getPosition());
        cursor.increment();
        return c;
    }

    public void setPosition(int position) {
        cursor.setPosition(position);
    }

    public int getPosition() {
        return cursor.getPosition();
    }

    public void skipDelimiters() {
        skip(delimiters);
    }

    public void skip(CodePointMatcher delimiters) {
        check(!cursor.end(), "Cursor is at the end. There is no next character.");
        int pos = cursor.getPosition();
        int indexFrom = cursor.getPosition();
        int indexTo = cursor.getUpperBound();
        for (int i = indexFrom; i < indexTo; ) {
            int current = text.codePointAt(i);
            if (!delimiters.matches(current)) {
                break;
            }
            int chars = Character.charCount(current);
            pos += chars;
            i += chars;
        }
        cursor.setPosition(pos);
    }

    public String next(CodePointMatcher matcher) {
        return stringify(builder -> next(matcher, builder));
    }

    public void next(CodePointMatcher matcher, StringBuilder builder) {
        expectedPositionChange(
                () -> nextOptional(matcher, builder),
                "Could not match next value"
        );
    }

    public String nextOptional(CodePointMatcher matcher) {
        return stringify(builder -> nextOptional(matcher, builder));
    }

    public void nextOptional(CodePointMatcher matcher, StringBuilder builder) {
        int pos = cursor.getPosition();
        int indexFrom = cursor.getPosition();
        int indexTo = cursor.getUpperBound();
        for (int i = indexFrom; i < indexTo; ) {
            int current = text.codePointAt(i);
            if (!matcher.matches(current)) {
                break;
            }
            builder.appendCodePoint(current);
            int chars = Character.charCount(current);
            pos += chars;
            i += chars;
        }
        cursor.setPosition(pos);
    }

    public String nextToken() {
        return stringify(this::nextToken);
    }

    public void nextToken(StringBuilder builder) {
        expectedPositionChange(
                () -> nextOptionalToken(builder),
                "Next token not found"
        );
    }

    public String nextOptionalToken() {
        return stringify(this::nextOptionalToken);
    }

    public void nextOptionalToken(StringBuilder builder) {
        if (cursor.end()) {
            return;
        }
        int c = peekNextCodePoint();
        if (quotes.matches(c)) {
            nextOptionalQuotedToken(builder);
        } else {
            nextOptionalUnquotedToken(builder);
        }
    }

    public String nextUnquotedToken() {
        return stringify(this::nextUnquotedToken);
    }

    public void nextUnquotedToken(StringBuilder builder) {
        expectedPositionChange(
                () -> nextOptionalUnquotedToken(builder),
                "Next unquoted token not found"
        );
    }

    public String nextOptionalUnquotedToken() {
        return stringify(this::nextOptionalUnquotedToken);
    }

    public void nextOptionalUnquotedToken(StringBuilder builder) {
        if (cursor.end()) {
            return;
        }
        int pos = cursor.getPosition();
        int indexFrom = cursor.getPosition();
        int indexTo = cursor.getUpperBound();
        boolean escaped = false;
        for (int i = indexFrom; i < indexTo; i++) {
            char current = text.charAt(i);
            if (delimiters.matches(current) || (escaped && quotes.matches(current))) {
                break;
            }
            escaped = escapes.matches(current);
            pos++;
            builder.append(current);
        }
        cursor.setPosition(pos);
    }

    public String nextQuotedToken() {
        return stringify(this::nextQuotedToken);
    }

    public void nextQuotedToken(StringBuilder builder) {
        expectedPositionChange(
                () -> nextOptionalQuotedToken(builder),
                "Next quoted token not found"
        );
    }

    public String nextOptionalQuotedToken() {
        return stringify(this::nextOptionalQuotedToken);
    }

    public void nextOptionalQuotedToken(StringBuilder builder) {
        if (cursor.end()) {
            return;
        }
        int pos = cursor.getPosition();
        int indexFrom = cursor.getPosition();
        int indexTo = cursor.getUpperBound();
        int quote = text.codePointAt(pos);
        if (!quotes.matches(quote)) {
            return;
        }
        pos++;
        indexFrom++;
        int escaped = -1;
        boolean ended = false;
        for (int i = indexFrom; i < indexTo; ) {
            int current = text.codePointAt(i);
            int chars = Character.charCount(current);
            if (escaped > 0) {
                if (current != quote && !escapes.matches(current)) {
                    builder.appendCodePoint(escaped);
                }
                builder.append(current);
                escaped = -1;
            } else {
                if (current == quote) {
                    ended = true;
                    pos += chars;
                    break;
                }
                if (escapes.matches(current)) {
                    escaped = current;
                } else {
                    builder.append(current);
                }
            }
            pos += chars;
            i += chars;
        }
        assertThat(ended, "Unpaired quotes when retrieving quoted token");
        cursor.setPosition(pos);
    }

    private void expectedPositionChange(Runnable runnable, String errorMessage) {
        int position = cursor.getPosition();
        runnable.run();
        assertThat(position < cursor.getPosition(), errorMessage);
    }

    private String stringify(Consumer<StringBuilder> appender) {
        StringBuilder builder = new StringBuilder();
        appender.accept(builder);
        return builder.toString();
    }

    private static class Cursor {
        static Cursor beginningOf(String text) {
            return new Cursor(0, text.codePointCount(0, text.length()));
        }

        private final int lowerBound;
        private final int upperBound;
        private int position;

        private Cursor(int lowerBound, int upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
            this.position = lowerBound;
        }

        int getLowerBound() {
            return lowerBound;
        }

        int getUpperBound() {
            return upperBound;
        }

        int getPosition() {
            return position;
        }

        void setPosition(int position) {
            if (position < this.lowerBound) {
                throw new IndexOutOfBoundsException("position: " + position + " < lowerBound: " + this.lowerBound);
            }
            if (position > this.upperBound) {
                throw new IndexOutOfBoundsException("position: " + position + " > upperBound: " + this.upperBound);
            }
            this.position = position;
        }

        void increment() {
            setPosition(position + 1);
        }

        void increment(int step) {
            setPosition(position + step);
        }

        void decrement() {
            setPosition(position - 1);
        }

        void decrement(int step) {
            setPosition(position - step);
        }

        boolean beginning() {
            return position <= lowerBound;
        }

        boolean middle() {
            return !beginning() && !end();
        }

        boolean end() {
            return position >= upperBound;
        }

        @Override
        public String toString() {
            return "Cursor{" +
                    "position=" + position +
                    ", upperBound=" + upperBound +
                    ", lowerBound=" + lowerBound +
                    '}';
        }
    }

    public static TokenizerBuilder builder(String text) {
        return new TokenizerBuilder(text);
    }

    public static class TokenizerBuilder {
        private final String text;
        private CodePointMatcher escapes = CodePointMatcher.is('\\');
        private CodePointMatcher quotes = QUOTES;
        private CodePointMatcher delimiters = WHITESPACES;

        public TokenizerBuilder(String text) {
            this.text = checkNotNull(text, "text");
        }

        public TokenizerBuilder noQuotes() {
            this.quotes = CodePointMatcher.none();
            return this;
        }

        public TokenizerBuilder quotes(char... quotes) {
            checkNotNull(quotes, "quotes");
            this.quotes = CodePointMatcher.anyOf(quotes);
            return this;
        }

        public TokenizerBuilder quotes(CodePointMatcher quotes) {
            checkNotNull(quotes, "quotes");
            this.quotes = quotes;
            return this;
        }

        public TokenizerBuilder noDelimiters() {
            this.delimiters = CodePointMatcher.none();
            return this;
        }

        public TokenizerBuilder delimiters(char... delimiters) {
            checkNotNull(delimiters, "delimiters");
            this.delimiters = CodePointMatcher.anyOf(delimiters);
            return this;
        }

        public TokenizerBuilder delimiters(CodePointMatcher delimiters) {
            checkNotNull(delimiters, "delimiters");
            this.delimiters = delimiters;
            return this;
        }

        public TokenizerBuilder noEscape() {
            this.escapes = CodePointMatcher.none();
            return this;
        }

        public TokenizerBuilder escapes(char... escapes) {
            checkNotNull(escapes, "escapes");
            this.escapes = CodePointMatcher.anyOf(escapes);
            return this;
        }

        public TokenizerBuilder escapes(CodePointMatcher escapes) {
            checkNotNull(escapes, "escapes");
            this.escapes = escapes;
            return this;
        }

        public Tokenizer build() {
            return new Tokenizer(text, quotes, delimiters, escapes);
        }
    }
}
