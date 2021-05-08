import kotlin.random.Random

class Test {
    fun test() {
        println("Transformations will begin from here.... Or not...?")

        if (Random.nextBoolean()) {
            println("Returning early!")
            return
        }

        println("Decided not to return... Program continuing as normal")
    }
}