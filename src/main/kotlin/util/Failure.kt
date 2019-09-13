package util

sealed class Failure : Exception() {
    object BestPathNotFound : Failure()
    object PreRequisitesMissing : Failure()
}