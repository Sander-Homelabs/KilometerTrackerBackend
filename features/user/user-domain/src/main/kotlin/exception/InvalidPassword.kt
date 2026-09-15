package exception

class InvalidPassword(
    reasons: List<String>
) : Exception("Invalid password: ${reasons.joinToString(", ")}")
