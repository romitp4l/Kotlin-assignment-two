package com.example.kotlin_assignment_one_again

// import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.kotlin_assignment_one_again.ui.theme.KotlinassignmentoneagainTheme
import org.json.JSONObject
import android.content.Context;
import android.widget.Toast;

import androidx.compose.material.icons.filled.Send as Send1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinassignmentoneagainTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(::sendDataToApi,this)
                }
            }
        }
    }
// Sending data to the server
    private fun sendDataToApi(phoneNumber: String, password: String, context: android.content.Context){
        val queue = Volley.newRequestQueue(context)
    val url = "http://192.168.29.135/membershipApp/login.php" // Replace with your actual API endpoint URL

    //creating string request
    val stringRequest = object : StringRequest(
        Request.Method.POST,url,
        { response ->
            try {
                // Convert the response string to a JSON object
                val jsonResponse = JSONObject(response)

                // Check the 'status' key in the JSON response
                val status = jsonResponse.getBoolean("status")

                if (status) {
                    // Handle the response from the server
                    Toast.makeText(context, "Successfully data sent to server $response", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Response: Data not inserted in the database.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error parsing JSON response.", Toast.LENGTH_SHORT).show()
            }
        },
        { error ->
            // Handle errors
            Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
        }) {
        override fun getParams(): Map<String, String> {
            // Set the parameters to be sent to the server
            val params: MutableMap<String, String> = HashMap()
            params["mobileNo"] = phoneNumber
            params["password"] = password

            return params
        }
    }

    // Add the request to the RequestQueue.
    queue.add(stringRequest)
}
}

@Composable
fun LoginScreen(sendDataToApi: (String, String, Context) -> Unit, context: Context) {
    var phoneNumber by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Enter Your Mobile no.") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Enter Your Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = {
                // Call the function reference when the button is clicked
                sendDataToApi(phoneNumber.text.toString().trim(), password.text.toString().trim(), context)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(top = 16.dp),
        ) {
            Icon(imageVector = Icons.Default.Send1, contentDescription = null)
            Text("Send data to database")
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    KotlinassignmentoneagainTheme {
//        Greeting("Android")
//    }
//}