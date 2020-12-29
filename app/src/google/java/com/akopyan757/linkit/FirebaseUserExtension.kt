
object FirebaseUserExtension {
    
    fun existServiceProvider(firebaseUser: FirebaseUser): Boolean {
        return firebaseUser.providerData.find { userInfo ->
            userInfo.providerId == GoogleAuthProvider.PROVIDER_ID
        } != null
    }
}