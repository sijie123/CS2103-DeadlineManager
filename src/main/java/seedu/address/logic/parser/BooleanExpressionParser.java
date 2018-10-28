package seedu.address.logic.parser;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import seedu.address.logic.parser.exceptions.BooleanExpressionParseException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses a boolean expression into a predicate.
 */
public class BooleanExpressionParser<T> {

    /**
     * Represents a boolean operator.
     */
    private static class BooleanOperator {
    }
    /**
     * Represents a left bracket.
     */
    private static class LeftBracket extends BooleanOperator {
    }
    /**
     * Represents a right bracket.
     */
    private static class RightBracket extends BooleanOperator {
    }
    /**
     * Represents an operator that has precedence and can operate on the output stack.
     */
    private abstract static class PrecedableOperator extends BooleanOperator {
        /**
         * Gets the precedence of this operator.
         * Lower value is evaluated earlier.
         */
        public abstract int precedence();
        /**
         * Gets the precedence of this operator.
         * Lower value is evaluated earlier.
         */
        public abstract <E> void operate(Deque<Predicate<E>> outputStack);
    }
    /**
     * Represents a binary operator.
     */
    private abstract static class BinaryOperator extends PrecedableOperator {
        @Override
        public <E> void operate(Deque<Predicate<E>> outputStack) {
            assert outputStack.size() >= 2 : "Not enough elements on output stack for binary operator";
            Predicate<E> o2 = outputStack.poll();
            Predicate<E> o1 = outputStack.poll();
            outputStack.push(operate(o1, o2));
        }
        public abstract <E> Predicate<E> operate(Predicate<E> operand1, Predicate<E> operand2);
    }
    /**
     * Represents a AND operator.
     */
    private static class AndOperator extends BinaryOperator {
        @Override
        public int precedence() {
            return 21;
        }
        public <E> Predicate<E> operate(Predicate<E> operand1, Predicate<E> operand2) {
            return operand1.and(operand2);
        }
    }
    /**
     * Represents a OR operator.
     */
    private static class OrOperator extends BinaryOperator {
        @Override
        public int precedence() {
            return 22;
        }
        public <E> Predicate<E> operate(Predicate<E> operand1, Predicate<E> operand2) {
            return operand1.or(operand2);
        }
    }
    /**
     * Represents a prefix unary operator.
     */
    private abstract static class PrefixUnaryOperator extends PrecedableOperator {
        @Override
        public <E> void operate(Deque<Predicate<E>> outputStack) {
            assert !outputStack.isEmpty() : "Not enough elements on output stack for binary operator";
            outputStack.push(operate(outputStack.poll()));
        }
        public abstract <E> Predicate<E> operate(Predicate<E> operand);
    }
    /**
     * Represents a NOT operator.
     */
    private static class NotOperator extends PrefixUnaryOperator {
        @Override
        public int precedence() {
            return 11;
        }
        public <E> Predicate<E> operate(Predicate<E> operand) {
            return operand.negate();
        }
    }

    /**
     * Helper class for boolean operators.
     */
    private static class BooleanOperators {
        static final LeftBracket LEFT_BRACKET = new LeftBracket();
        static final RightBracket RIGHT_BRACKET = new RightBracket();
        static final AndOperator AND = new AndOperator();
        static final OrOperator OR = new OrOperator();
        static final NotOperator NOT = new NotOperator();

        static final Predicate<Character> OPERATOR_CHAR_PREDICATE = ch -> ch == '(' || ch == ')'
            || ch == '&' || ch == '|' || ch == '!';

        /**
         * Creates an operator from the next token of the given StringTokenizer.
         */
        public static BooleanOperator parse(StringTokenizer tokenizer) throws NoSuchElementException {
            BooleanOperator operator = null;
            if (operator == null) {
                if (tokenizer.tryNextPattern(Pattern.compile("\\(")) != null) {
                    operator = LEFT_BRACKET;
                }
            }
            if (operator == null) {
                if (tokenizer.tryNextPattern(Pattern.compile("\\)")) != null) {
                    operator = RIGHT_BRACKET;
                }
            }
            if (operator == null) {
                if (tokenizer.tryNextPattern(Pattern.compile("\\&{1,2}")) != null) {
                    operator = AND;
                }
            }
            if (operator == null) {
                if (tokenizer.tryNextPattern(Pattern.compile("\\|{1,2}")) != null) {
                    operator = OR;
                }
            }
            if (operator == null) {
                if (tokenizer.tryNextPattern(Pattern.compile("\\!")) != null) {
                    operator = NOT;
                }
            }
            if (operator == null) {
                throw new InputMismatchException("No matching operator found!");
            }
            return operator;
        }

        /**
         * Creates an operator from the next token of the given StringTokenizer,
         * or returns null if the next token cannot be interpreted as any known operator.
         */
        public static BooleanOperator tryParse(StringTokenizer tokenizer) throws NoSuchElementException {
            try {
                return parse(tokenizer);
            } catch (InputMismatchException e) {
                return null;
            }
        }
    }

    /**
     * Represents a the function to execute to parse an operand.
     */
    @FunctionalInterface
    public interface OperandParser<U> {
        public Predicate<U> parse(StringTokenizer tokenizer, Predicate<Character> reservedChar) throws ParseException;
    }

    private final OperandParser<T> operandParser;

    BooleanExpressionParser(OperandParser<T> operandParser) {
        this.operandParser = operandParser;
    }

    /**
     * Inserts a prefix unary operator as per the shunting yard algorithm.
     */
    private static <T> void insertPrefixUnaryOperator(ArrayDeque<BooleanOperator> operatorStack,
                                                      ArrayDeque<Predicate<T>> outputStack,
                                                      PrefixUnaryOperator operator) {
        operatorStack.push(operator);
    }

    /**
     * Inserts a binary operator as per the shunting yard algorithm.
     */
    private static <T> void insertBinaryOperator(ArrayDeque<BooleanOperator> operatorStack,
                                                 ArrayDeque<Predicate<T>> outputStack, BinaryOperator operator) {
        while (!operatorStack.isEmpty()
            && operatorStack.peek() instanceof PrecedableOperator
            && ((PrecedableOperator) operatorStack.peek()).precedence()
            <= operator.precedence()) {
            PrecedableOperator precOperator = (PrecedableOperator) operatorStack.poll();
            precOperator.operate(outputStack);
        }
        operatorStack.push(operator);
    }

    /**
     * Inserts a left bracket as per the shunting yard algorithm.
     */
    private static <T> void insertLeftBracket(ArrayDeque<BooleanOperator> operatorStack,
                                              ArrayDeque<Predicate<T>> outputStack, LeftBracket operator) {
        operatorStack.push(operator);
    }

    /**
     * Inserts a right bracket as per the shunting yard algorithm.
     */
    private static <T> void insertRightBracket(ArrayDeque<BooleanOperator> operatorStack,
                                               ArrayDeque<Predicate<T>> outputStack, RightBracket operator)
                                               throws BooleanExpressionParseException {
        while (!operatorStack.isEmpty() && operatorStack.peek() instanceof PrecedableOperator) {
            PrecedableOperator precOperator = (PrecedableOperator) operatorStack.poll();
            precOperator.operate(outputStack);
        }
        if (operatorStack.isEmpty()) {
            throw new BooleanExpressionParseException(
                "The right bracket was encountered without matching left bracket");
        }
        assert operatorStack.peek() instanceof LeftBracket : "Expected a left bracket";
        operatorStack.pop();
    }

    /**
     * Inserts a binary operator as per the shunting yard algorithm.
     */
    private static <T> void insertImplicitBinaryOperator(ArrayDeque<BooleanOperator> operatorStack,
                                                         ArrayDeque<Predicate<T>> outputStack) {
        insertBinaryOperator(operatorStack, outputStack, BooleanOperators.AND);
    }

    /**
     * Inserts an operand to the output stack as per the shunting yard algorithm.
     */
    private static <T> void insertOperand(ArrayDeque<BooleanOperator> operatorStack,
                                          ArrayDeque<Predicate<T>> outputStack, Predicate<T> operand) {
        outputStack.push(operand);
    }

    /**
     * Inserts an operand to the output stack as per the shunting yard algorithm.
     */
    private static <T> void cleanUp(ArrayDeque<BooleanOperator> operatorStack, ArrayDeque<Predicate<T>> outputStack)
        throws BooleanExpressionParseException {
        while (!operatorStack.isEmpty() && operatorStack.peek() instanceof PrecedableOperator) {
            PrecedableOperator precOperator = (PrecedableOperator) operatorStack.poll();
            precOperator.operate(outputStack);
        }

        if (!operatorStack.isEmpty()) {
            assert operatorStack.peek() instanceof LeftBracket : "Expected a left bracket";
            throw new BooleanExpressionParseException("Left bracket does not have matching right bracket");
        }
    }

    /**
     * Parses the given string as a filter expression.
     * This method uses the shunting yard algorithm, with the ability to parse binary and unary operators.
     * This method is long because this algorithm cannot be easily broken into multiple methods.
     */
    public Predicate<T> parse(String str) throws ParseException {
        StringTokenizer tokenizer = new StringTokenizer(str);

        ArrayDeque<BooleanOperator> operatorStack = new ArrayDeque<>();
        ArrayDeque<Predicate<T>> outputStack = new ArrayDeque<>();

        boolean isExpectingOperand = true;

        while (tokenizer.hasNextToken()) {
            BooleanOperator operator = BooleanOperators.tryParse(tokenizer);
            if (operator != null) {
                // store operator
                if (operator instanceof PrefixUnaryOperator) {
                    if (!isExpectingOperand) {
                        // two operands are adjacent, so we insert the implicit operator (which is the AND operator)
                        insertImplicitBinaryOperator(operatorStack, outputStack);
                        // throw new BooleanExpressionParseException("The " + operator.getClass().getSimpleName()
                        //     + " operator cannot be preceded by an operand");
                    }
                    insertPrefixUnaryOperator(operatorStack, outputStack, (PrefixUnaryOperator) operator);
                    isExpectingOperand = true;
                } else if (operator instanceof BinaryOperator) {
                    if (isExpectingOperand) {
                        throw new BooleanExpressionParseException("The " + operator.getClass().getSimpleName()
                            + " operator cannot be preceded by another operator");
                    }
                    insertBinaryOperator(operatorStack, outputStack, (BinaryOperator) operator);
                    isExpectingOperand = true;
                } else if (operator instanceof LeftBracket) {
                    if (!isExpectingOperand) {
                        // two operands are adjacent, so we insert the implicit operator (which is the AND operator)
                        insertImplicitBinaryOperator(operatorStack, outputStack);
                        // throw new BooleanExpressionParseException("The " + operator.getClass().getSimpleName()
                        //     + " operator cannot be preceded by an operand");
                    }
                    insertLeftBracket(operatorStack, outputStack, (LeftBracket) operator);
                    isExpectingOperand = true;
                } else if (operator instanceof RightBracket) {
                    if (isExpectingOperand) {
                        throw new BooleanExpressionParseException("The " + operator.getClass().getSimpleName()
                            + " operator cannot be preceded by another operator");
                    }
                    insertRightBracket(operatorStack, outputStack, (RightBracket) operator);
                    isExpectingOperand = false;
                }
            } else {
                // read and store operand
                if (!isExpectingOperand) {
                    // two operands are adjacent, so we insert the implicit operator (which is the AND operator)
                    insertImplicitBinaryOperator(operatorStack, outputStack);
                    // throw new BooleanExpressionParseException("Two operands cannot be adjacent");
                }
                insertOperand(operatorStack, outputStack,
                    operandParser.parse(tokenizer, BooleanOperators.OPERATOR_CHAR_PREDICATE));
                isExpectingOperand = false;
            }
        }

        if (isExpectingOperand) {
            throw new BooleanExpressionParseException("Operand expected at end of expression");
        }

        cleanUp(operatorStack, outputStack);

        assert outputStack.size() == 1 : "Shunting yard algorithm error in BooleanExpressionParser";

        return outputStack.poll();
    }

}
