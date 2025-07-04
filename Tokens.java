package analizador2;


public enum Tokens {

    // ───────────────────────────────────────────────
    // Python-Inspired Keywords
    // ───────────────────────────────────────────────
    IF,             // If condition
    ELIF,           // Else-if condition
    ELSE,           // Else condition
    WHILE,          // While loop
    FOR,            // For loop
    IN,             // In keyword (for iteration, membership)
    RETURN,         // Return statement
    PRINT,          // Print to console
    TRUE,           // Boolean true
    FALSE,          // Boolean false
    NONE,           // Null/None value
    AS,             // Alias import or casting
    FROM,           // Import from module
    IMPORT,         // LIbrerias
    PASS,           // No-op statement (placeholder)
    BREAK,          // Break loop
    CONTINUE,       // Continue to next iteration
    DEF,
    INPUT,
    MAIN,

    // ───────────────────────────────────────────────
    // Rust-Inspired Keywords
    // ───────────────────────────────────────────────
    FN,             // Function definition
    LET,            // Variable declaration (immutable by default)
    MUT,            // Mutable variable declaration
    STRUCT,         // Struct declaration
    IMPL,           // Implementation block
    MATCH,          // Pattern matching
    LOOP,           // Infinite loop (Rust style)
    ENUM,           // Enumeration
    MOD,            // Module declaration
    PUB,            // Public visibility
    REF,            // Reference
    SELF,           // Current instance
    SUPER,          // Super module
    USE,            // Import module
    CRATE,          // Current crate/module
    CONST,          // Constant declaration
    STATIC,         // Static variable
    TYPE,           // Type alias
    TRAIT,          // Trait declaration
    ASYNC,          // Asynchronous function/block
    AWAIT,          // Await on async
    MOVE,           // Move semantics
    UNSAFE,         // Unsafe block

    // ───────────────────────────────────────────────
    // Primitive Data Types
    // ───────────────────────────────────────────────
    INT,            // Integer type
    FLOAT,          // Floating-point number
    BOOL,           // Boolean type
    STRING,         // String type
    CHAR,           // Character (e.g., 'a')
    BYTE,           // Byte type (e.g., b'a')
    UNIT,           // Unit type (void-like, Rust's ())

    // ───────────────────────────────────────────────
    // Compound / Abstract Data Types
    // ───────────────────────────────────────────────
    VECTOR,         // Dynamic array or list
    TUPLE,          // Fixed-size ordered grouping
    OPTION,         // Optional value (Some/None)
    RESULT,         // Result type for error handling
    MAP,            // Dictionary / hashmap
    SET,            // Unique value collection
    ARRAY,          // Fixed-size array

    // ───────────────────────────────────────────────
    // Literals
    // ───────────────────────────────────────────────
    INTEGER_LITERAL,    // e.g., 42
    FLOAT_LITERAL,      // e.g., 3.14
    STRING_LITERAL,     // e.g., "hello"
    BOOL_LITERAL,       // true or false
    CHAR_LITERAL,       // e.g., 'x'
    BYTE_LITERAL,       // e.g., b'x'
    NULL_LITERAL,       // Represents None/null/undefined

    // ───────────────────────────────────────────────
    // Operators (Arithmetic, Comparison, Logical)
    // ───────────────────────────────────────────────
    PLUS,               // +
    MINUS,              // -
    MULTIPLY,           // *
    DIVIDE,             // /
    MODULO,             // %
    POWER,              // **

    EQUALS,             // ==
    NOT_EQUALS,         // !=
    GREATER_THAN,       // >
    LESS_THAN,          // <
    GREATER_EQUALS,     // >=
    LESS_EQUALS,        // <=

    AND,                // &&
    OR,                 // ||
    NOT,                // !

    ASSIGN,             // =
    PLUS_ASSIGN,        // +=
    MINUS_ASSIGN,       // -=
    MULTIPLY_ASSIGN,    // *=
    DIVIDE_ASSIGN,      // /=
    MODULO_ASSIGN,      // %=

    // ───────────────────────────────────────────────
    // Delimiters and Punctuation
    // ───────────────────────────────────────────────
    LEFT_PAREN,         // (
    RIGHT_PAREN,        // )
    LEFT_BRACE,         // {
    RIGHT_BRACE,        // }
    LEFT_BRACKET,       // [
    RIGHT_BRACKET,      // ]
    SEMICOLON,          // ;
    COLON,              // :
    COMMA,              // ,
    DOT,                // .
    ARROW,              // ->

    // ───────────────────────────────────────────────
    // Identifiers and Special
    // ───────────────────────────────────────────────
    IDENTIFIER,         // Names for variables, functions, etc.
    ERROR               // Invalid/unrecognized token
}
//94