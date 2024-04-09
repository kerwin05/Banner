package com.example.banner.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.blankj.utilcode.util.NetworkUtils
import com.example.banner.R
import com.example.banner.adapter.BannerAdapter
import com.example.banner.databinding.ActivityMainBinding
import com.example.banner.model.BannerModel
import com.example.banner.utils.DeviceUtil
import com.example.banner.utils.eventbus.NetworkEvent
import com.example.banner.utils.log
import com.zhpan.bannerview.transform.toPx
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: BannerAdapter

    private val mBanners = ArrayList<BannerModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        EventBus.getDefault().register(this)

        initData()
    }

    override fun onResume() {
        super.onResume()
        initBanner()

        DeviceUtil.getIdAndPass { id, pass ->
            "DeviceUtil getIdAndPass id:${id}, pass:$pass".log()
        }
        DeviceUtil.getApSSIDAndPwd(this) { id, pass ->
            "DeviceUtil getApSSIDAndPwd id:${id}, pass:$pass".log()
        }
        val getCurrentWifiInfo = DeviceUtil.getCurrentWifiInfo(this)
        "DeviceUtil getCurrentWifiInfo id:${getCurrentWifiInfo.first}, pass:${getCurrentWifiInfo.second}".log()
    }

    private fun initBanner() {
        mAdapter = BannerAdapter(this)
        binding.banner.apply {
            setCanLoop(true)
            setAutoPlay(true)
            setAdapter(mAdapter)
            setIndicatorSliderRadius(5.toPx())
            setIndicatorMargin(0, 0, 0, 20.toPx())
            setIndicatorSliderColor(
                resources.getColor(R.color.purple_200, null),
                resources.getColor(R.color.black, null)
            )
        }.create(mBanners)

        binding.tv.isVisible = false

    }

    //初始化数据，后续可以将图片更改为从文件拿
    private fun initData() {
        mBanners.add(
            BannerModel(
                R.drawable.image1
            ).apply { refreshIp() }
        )
        mBanners.add(
            BannerModel(
                R.drawable.image2
            ).apply { refreshIp() }
        )
        mBanners.add(
            BannerModel(
                R.drawable.image3
            ).apply { refreshIp() }
        )
        mBanners.add(
            BannerModel(
                R.drawable.image4
            ).apply { refreshIp() }
        )
        refreshBannerIp()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun rcvMsg(event: NetworkEvent) {
        //EvenBus收到网络改变的通知，刷新Banner显示的Ip
        refreshBannerIp()
    }

    private fun refreshBannerIp() {
        NetworkUtils.getIPAddressAsync(true) {
            //刷新Banner数据的Ip
            mBanners.forEach { model ->
                model.refreshIp(it)
            }
            //数据更新banner
            val cur = binding.banner.currentItem
            binding.banner.refreshData(mBanners)
            binding.banner.setCurrentItem(cur, false)
        }
    }

    private val TAG = ""
//    val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context, intent: Intent) {
//            val action = intent.action
//            Log.d(TAG, "action=$action")
//            if (action == WifiManager.WIFI_AP_STATE_CHANGED_ACTION) {
//                val apStat = intent.getIntExtra("wifi_state", SystemUtil.WIFI_AP_STATE_ENABLED)
//                Log.d(TAG, "apStat=$apStat")
//                if (SystemUtil.WIFI_AP_STATE_ENABLED == apStat) {
//                    val wc = SystemUtil.getWifiApConfiguration(mWifiManager)
//                    mPsw = wc.preSharedKey
//                    mSsid = wc.SSID
//                    mWifiOn = true
//                    if (mWifiTest) {
//                        mPsw = SystemUtil.FACTORY_TEST_PSW
//                        mSsid = SystemUtil.FACTORY_TEST_SSID
//                    }
//                    notifyWifiChange(mSsid, mPsw, mWifiOn)
//                    sendWorkMsg(MSG_HOSTAPD_CLI_START)
//                } else {
//                    mWifiOn = false
//                    notifyWifiChange(mSsid, mPsw, mWifiOn)
//                    sendWorkMsg(MSG_DETERMIN_CHANGE)
//                }
//            } else if (action == WifiManager.WIFI_AP_CONFIG_CHANGED_ACTION) {
//                val wc = SystemUtil.getWifiApConfiguration(mWifiManager)
//                mPsw = wc.preSharedKey
//                mSsid = wc.SSID
//                notifyWifiChange(mSsid, mPsw, mWifiOn)
//            } else if (action == SystemUtil.ACTION_WIFI_TIMEOUT_CHANGED) {
//                getTimeout()
//                removeAllWorkMsg()
//                dismissUi()
//                startTick()
//                sendWorkMsg(MSG_DETERMIN_CHANGE)
//            } else if (action == SystemUtil.ACTION_WMT_PAIRED) {
//                sendWorkMsg(MSG_UPDATE_WMT)
//            } else if (action == SystemUtil.ACTION_CLEAR_HISTORY_CHANGED) {
//                mClearHistoryType = SystemUtil.getClearHistory()
//                Log.d(TAG, "mClearHistoryType changed=$mClearHistoryType")
//            } else if (action == Action.ACTION_REFRESH_PASSWORD) {
//                val stringExtra = intent.getStringExtra(Action.EXTRA_DATA)
//                if (!TextUtils.isEmpty(stringExtra)) {
//                    manualPass = stringExtra
//                    manual = true
//                }
//                refreshPass()
//                Log.d(TAG, "refreshPass manual = $manual")
//            } else if (action == WifiService.ACTION_UPDATE_NETWORK_INFO) {
//                refreshQrCode()
//                Log.d(TAG, "BroadcastReceiver>>> ACTION_UPDATE_NETWORK_INFO")
//            }
//        }
//    }

}