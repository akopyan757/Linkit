package com.akopyan757.linkit.model.exception

import java.lang.Exception

class FolderExistsException: Exception("A folder with this name already exists")

class UrlIsNotValidException: Exception("The url is not valid")

class FirebaseUserNotFound: Exception("Firebase user not found")