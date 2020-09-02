package com.example.qi.gaodetest

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amap.api.maps.AMap
import com.amap.api.maps.model.MyLocationStyle
import kotlinx.android.synthetic.main.activity_map_test.*


class MapTestActivity : AppCompatActivity() {

    var map: AMap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_test)
        mMapView.onCreate(savedInstanceState)
        initMap()
//        requestLocationPermission()
        showMyLocationPoint() // 显示定位蓝点
    }
//
//    private fun requestLocationPermission() {
//        if (ContextCompat.checkSelfPermission(MapTestActivity@ this, Manifest.permission.ACCESS_FINE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED) {//未开启定位权限
//            //开启定位权限,200是标识码
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 200
//            );
//        } else {
////            startLocaion();//开始定位
//            Toast.makeText(this, "已开启定位权限", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String?>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            200 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { //用户同意权限,执行我们的操作
////                startLocaion() //开始定位
//            } else { //用户拒绝之后,当然我们也可以弹出一个窗口,直接跳转到系统设置页面
//                Toast.makeText(this, "未开启定位权限,请手动到设置去开启权限", Toast.LENGTH_LONG).show()
//            }
//            else -> {
//            }
//        }
//    }

    private fun showMyLocationPoint() {
        val myLocationStyle: MyLocationStyle
        myLocationStyle =
            MyLocationStyle() //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        map?.setMyLocationStyle(myLocationStyle) //设置定位蓝点的Style
//        map?.getUiSettings()!!.isMyLocationButtonEnabled = true //设置默认定位按钮是否显示，非必需设置。
        map?.setMyLocationEnabled(true) // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    private fun initMap() {
        if (map == null) {
            map = mMapView.map
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mMapView.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }

}

