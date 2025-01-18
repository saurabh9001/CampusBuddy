package com.example.mysql

import android.util.Log
import java.sql.Connection
import java.sql.DriverManager

object ConnectionClass {
    //sudo /usr/local/mysql/support-files/mysql.server start
    ///10.0.2.2
    private const val db = "saurabh"
    private const val ip = "192.168.132.32"
    private const val port = "3306"
    private const val username = "smdb1"
    private const val password = "12345678"

    fun CONN(): Connection? {
        var conn: Connection? = null
        try {
            Class.forName("com.mysql.jdbc.Driver")
            val connectionString = "jdbc:mysql://$ip:$port/$db"
            // Establish a connection to a MySQL database using the JDBC driver
            conn = DriverManager.getConnection(connectionString, username, password)
        } catch (e: Exception) {
            Log.e("ERROR", e.message ?: "Unknown error")
        }
        return conn
    }
}
////### 5. Clean and Minimalistic
//- Primary Color: #2563EB (Blue)
//- Secondary Color: #9CA3AF (Gray)
//- Accent Color: #34D399 (Green)
//- Background Color: #FFFFFF (White)
//- Text Color: #1F2937 (Dark Gray)
