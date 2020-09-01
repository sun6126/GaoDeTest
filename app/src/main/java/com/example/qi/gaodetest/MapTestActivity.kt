package com.example.qi.gaodetest

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.AMap.CancelableCallback
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle
import kotlinx.android.synthetic.main.activity_map_test.*


class MapTestActivity : AppCompatActivity() {

    var mMapView: MapView? = null
    lateinit var aMap: AMap

    private var isFirstLocate = true

    // 声明AMapLocationClient类对象
    val mLocationClient: AMapLocationClient? = null

    // 异步获取定位结果
    val aMapLocationListener = AMapLocationListener { aMapLocation ->
        if (aMapLocation != null) {
            if (aMapLocation.errorCode == 0) {
                // 更新页面
                // updateUI(aMapLocation);

                // 定位当前
                navigateTo(aMapLocation)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_test)
        //获取地图控件引用
        mMapView = map as MapView
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView!!.onCreate(savedInstanceState);

        //初始化地图控制器对象
        aMap = mMapView!!.map

        // 申请权限
        val permissionList: MutableList<String> = ArrayList()
        if (ContextCompat.checkSelfPermission(
                this@MapTestActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (ContextCompat.checkSelfPermission(
                this@MapTestActivity,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE)
        }
        if (ContextCompat.checkSelfPermission(
                this@MapTestActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!permissionList.isEmpty()) {
            val permissions =
                permissionList.toTypedArray()
            // 一起申请
            ActivityCompat.requestPermissions(
                this@MapTestActivity,
                permissions,
                1
            )
        } else {
            // 开始定位
            requestLocation()
        }


        // 显示定位蓝点
//        showPositionWithBluePoint()
    }

    private fun showPositionWithBluePoint() {
        val myLocationStyle: MyLocationStyle
        myLocationStyle =
            MyLocationStyle() //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。

        myLocationStyle.interval(2000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle) //设置定位蓝点的Style

        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true) // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
        myLocationStyle.showMyLocation(true)

    }

    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView!!.onDestroy();
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView!!.onPause();
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView!!.onSaveInstanceState(outState);
    }

    /**
     * 启动定位
     */
    private fun requestLocation() {
        initLocation()
        mLocationClient!!.startLocation()
    }

    /**
     * 设置定位参数
     */
    private fun initLocation() {
        val option = AMapLocationClientOption()
        option.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        option.interval = 5000 // 2s跟新一次
        option.isNeedAddress = true // 获取精度较高的地址
        mLocationClient!!.setLocationOption(option)
    }

    /**
     * 动态申请权限的结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> if (grantResults.size > 0) {
                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(this, "必须同意所有的权限", Toast.LENGTH_SHORT).show()
                        // 结束活动
                        finish()
                        return
                    }
                }
                // 开始请求地里位置
                requestLocation()
            } else {
                Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show()
                finish()
            }
            else -> {
            }
        }
    }


    /**
     * 将地图视点移动到某一位置
     *
     * @param aMapLocation
     */
    private fun navigateTo(aMapLocation: AMapLocation) {
        if (isFirstLocate) {
            val curLng = LatLng(aMapLocation.latitude, aMapLocation.longitude)
            val mCameraUpdate = CameraUpdateFactory.newCameraPosition(
                CameraPosition(
                    curLng,  // location
                    18f,  // zoom
                    30f,  // 可视区域倾斜度
                    0f
                ) // 可视区域指向的方向
            )
            aMap.animateCamera(mCameraUpdate, 1000, object : CancelableCallback {
                override fun onFinish() {}
                override fun onCancel() {}
            })

            // 绘制点
            aMap.addMarker(MarkerOptions().position(curLng).title("我的位置").snippet("江苏科技大学"))

//            LatLng ll = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
//            aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
//            aMap.moveCamera(CameraUpdateFactory.changeLatLng(ll));
            isFirstLocate = false
        }
    }

    /**
     * 更新ui结果
     *
     * @param aMapLocation
     */
    private fun updateUI(aMapLocation: AMapLocation) {
        // 解析定位结果
        var currentPosition = StringBuilder();
        currentPosition.append("纬度:").append(aMapLocation.getLatitude()).append("\n");
        currentPosition.append("经度:").append(aMapLocation.getLongitude()).append("\n");
        currentPosition.append("精度:")
            .append(aMapLocation.getAccuracy())
            .append("\n");
        currentPosition.append("时间：")
            .append(aMapLocation.getTime())
            .append("\n");
        currentPosition.append("地址")
            .append(aMapLocation.getAddress())
            .append("\n");
        currentPosition.append("国家")
            .append(aMapLocation.getCountry())
            .append("\n");
        currentPosition.append("省份:")
            .append(aMapLocation.getProvince())
            .append("\n");
        currentPosition.append("城市:")
            .append(aMapLocation.getCity())
            .append("\n");
        currentPosition.append("城区:")
            .append(aMapLocation.getDistrict())
            .append("\n");
        currentPosition.append("街道:")
            .append(aMapLocation.getStreet())
            .append("\n");
        currentPosition.append("描述:").append(aMapLocation.getDescription()).append("\n");
        currentPosition.append("定位type:").append(aMapLocation.getLocationType()).append("\n");
        currentPosition.append("定位方式:");
        if (aMapLocation.getLocationType() == AMapLocation.LOCATION_TYPE_GPS) {
            currentPosition.append("GPS");
        } else if (aMapLocation.getLocationType() == AMapLocation.LOCATION_TYPE_WIFI) {
            currentPosition.append("网络");
        }
        textView.setText(currentPosition.toString());
    }
}

