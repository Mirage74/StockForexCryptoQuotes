import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

private const val DATASTORE_NAME = "user_prefs"

object EncryptedDataStoreManager {

    private val TOKEN_KEY = stringPreferencesKey("user_token")

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

    private fun getEncryptedFile(context: Context): EncryptedFile {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val file = context.preferencesDataStoreFile(DATASTORE_NAME)

        file.parentFile?.mkdirs()


        if (!file.exists()) {
            file.createNewFile()
        }

        return EncryptedFile.Builder(
            context,
            file,
            masterKey,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }

    suspend fun saveToken(context: Context, token: String) {
        //val file = File(context.filesDir, "user_prefs.preferences_pb")
        val file = context.preferencesDataStoreFile(DATASTORE_NAME)

        file.parentFile?.mkdirs()

        // Create a new file if it doesn't exist
        if (!file.exists()) {
            withContext(Dispatchers.IO) {
                file.createNewFile()
            }
        }

        // Write the token to the file
        withContext(Dispatchers.IO) {
            FileOutputStream(file).use { outputStream ->
                outputStream.write(token.toByteArray())
            }
        }

        // Optionally, you can also save the token in plain text in DataStore for comparison purposes
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

//    suspend fun saveToken(context: Context, token: String) {
//        // Create an encrypted file
//        val encryptedFile = getEncryptedFile(context)
//
//        // Open an output stream for the encrypted file
//        val encryptedOutputStream: OutputStream = encryptedFile.openFileOutput()
//
//        // Write the token to the encrypted output stream
//        encryptedOutputStream.use { outputStream ->
//            outputStream.write(token.toByteArray())
//        }
//
//        // Optionally, you can also save the token in plain text in DataStore for comparison purposes
//        context.dataStore.edit { preferences ->
//            preferences[TOKEN_KEY] = token
//        }
//    }

    private fun getTokenFlow(context: Context): Flow<String?> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[TOKEN_KEY]
            }
    }

    suspend fun getToken(context: Context): String? {
        return getTokenFlow(context)
            .firstOrNull()
    }

}
