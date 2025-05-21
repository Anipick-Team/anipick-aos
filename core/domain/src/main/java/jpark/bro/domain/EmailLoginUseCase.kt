package jpark.bro.domain

class EmailLoginUseCase() {
    suspend operator fun invoke(): Result<Boolean> {
        return Result.success(true)
    }
}