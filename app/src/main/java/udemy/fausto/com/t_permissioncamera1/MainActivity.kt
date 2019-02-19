package udemy.fausto.com.t_permissioncamera1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileInputStream
import java.util.jar.Manifest


private const val FILE_PICKER_ID = 12
private const val  PERMISSION_REQUEST = 1

class MainActivity : AppCompatActivity() {

    private var permissions = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE )


    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this


        btn_request.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (checkPermission(context, permissions)) {
                    Toast.makeText(context, "Permissions already granted you can press any button now", Toast.LENGTH_LONG).show()
                } else {

                    // método de Android fácil de usar, no necesita una función de adaptación, hay un callback onRequestPermissionResults
                    requestPermissions(permissions, PERMISSION_REQUEST)
                }
            } else {
                Toast.makeText(context, "Permissions already granted you can press any button now", Toast.LENGTH_LONG).show()

            }


        }




        btn_camera.setOnClickListener {
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            startActivity(intent)

        }


        btn_file.setOnClickListener {
            intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, FILE_PICKER_ID)
        }





    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_PICKER_ID && resultCode == Activity.RESULT_OK){

            val dest = File(data!!.data.path)

            val inputAsString = FileInputStream(dest).bufferedReader().use {
                it.readText() }

            btn_file.text = inputAsString

        }


    }

    // se usa este método propio más fácil de usar que el de Android (parecido a los parámetros del requestPermission : Boolean
    fun checkPermission(context: Context, permissionArray: Array<String>) : Boolean{


        return  false

    }

    // callback de RequestPermissions
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST) {
            var allSuccess = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED){
                    allSuccess = false

                    var requestAgain = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[i])

                    if (requestAgain) {
                        Toast.makeText(context, "Permission denied", Toast.LENGTH_LONG).show()
                    } else {

                        Toast.makeText(context, "Go to setting and enable the permission", Toast.LENGTH_LONG).show()
                    }

                }


            }
            if (allSuccess) {
                Toast.makeText(context, "Permission granted you can press buttons", Toast.LENGTH_LONG).show()
            }

        }
    }

}
