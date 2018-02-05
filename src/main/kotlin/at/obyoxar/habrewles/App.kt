package at.obyoxar.habrewles

import at.obyoxar.habrewles.data.Repository
import at.obyoxar.habrewles.events.EventProvider
import at.obyoxar.habrewles.persistance.RepositoryLoader

class App {
    fun launch(){
        RepositoryLoader().load()
        EventProvider.instance.start()
        Repository.instance.rootStateMachine.start(true)
        EventProvider.instance.stop()
    }
}

fun main(args: Array<String>){
    App().launch()
    println("App stopped gracefully.")
}