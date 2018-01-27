package at.obyoxar.habrewles.data

class Repository{

    val stateMachine: StateMachine

    private constructor(stateMachine: StateMachine){
        this.stateMachine = stateMachine
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