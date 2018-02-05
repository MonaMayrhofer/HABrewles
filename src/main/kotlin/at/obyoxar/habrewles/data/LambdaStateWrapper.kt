package at.obyoxar.habrewles.data

class LambdaStateWrapper(name: String,
                         val enableHandler: (StateMachineTraverser) -> Unit = {},
                         val disableHandler: (StateMachineTraverser) -> Unit = {}
) : State(name) {
    override fun disable(stateMachineTraverser: StateMachineTraverser) {
        disableHandler(stateMachineTraverser)
    }

    override fun enable(stateMachineTraverser: StateMachineTraverser) {
        enableHandler(stateMachineTraverser)
    }
}