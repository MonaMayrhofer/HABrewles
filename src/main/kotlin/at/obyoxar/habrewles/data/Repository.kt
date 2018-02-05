package at.obyoxar.habrewles.data

class Repository{

    val rootStateMachine: StateMachine

    private constructor(rootStateMachine: StateMachine){
        this.rootStateMachine = rootStateMachine
    }

    companion object {
        @Volatile private var INSTANCE: Repository? = null

        val instance: Repository
            get() {
                return INSTANCE ?: synchronized(this){
                    INSTANCE ?: throw RuntimeException("Repository must be created first")
                }
            }

        fun initialize(stateMachine: StateMachine){
            INSTANCE = Repository(stateMachine)
        }
    }
}