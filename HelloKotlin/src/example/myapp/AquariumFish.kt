package example.myapp

//abstract class AquariumFish {
//    abstract val color: String
//}

//class Shark : AquariumFish(), FishAction {
class Shark : FishAction, FishColor {
    override val color = "gray"
    override fun eat() {
        println("hunt and eat fish")
    }
}

//class Plecostomus : AquariumFish(), FishAction {
//class Plecostomus : FishAction, FishColor by GoldColor {
class Plecostomus(fishColor: FishColor = GoldColor) :
    FishAction by PrintingFishAction("eat algae"),
    FishColor by GoldColor {
    //    override val color = "gold"
//    override fun eat() {
//        println("eat algae")
//    }
}

interface FishAction {
    fun eat()
}

interface FishColor {
    val color: String
}

object GoldColor : FishColor {
    override val color = "gold"
}

class PrintingFishAction(val food: String) : FishAction {
    override fun eat() {
        println(food)
    }
}
