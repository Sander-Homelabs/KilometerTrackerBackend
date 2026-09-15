package exception

class MissingVariable(
    variables: List<String>
) : Exception("Missing variable${if (variables.size > 1) "s" else "" }: ${variables.joinToString(", ")}")
