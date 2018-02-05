package at.obyoxar.habrewles.data

class TerminatorState(name: String): State(name){
    override fun enable(stateMachineTraverser: StateMachineTraverser) {
        stateMachineTraverser.stop()
    }
}