package com.evdayapps.go_router.testdata.common

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class BaseActivity : AppCompatActivity() {

    abstract fun fragmentBuilder(
        pathParams : Map<String, String>,
        queryParams : Map<String, String>,
        arguments : Map<String, Any>
    ) : Fragment

}