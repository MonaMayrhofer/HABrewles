package at.obyoxar.habrewles.data

class NestedStateMachine(name: String, stateMachine: StateMachine) : State(name){

    val stateMachine: StateMachine = stateMachine

    override fun disable(stateMachineTraverser: StateMachineTraverser) {
        stateMachine.stop()
    }

    override fun enable(stateMachineTraverser: StateMachineTraverser) {
        stateMachine.start(false)
    }
}