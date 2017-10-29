package com.dvr.android.dvr.offlinemap;
///**
// * 
// */
//package com.yecon.android.dvr.offlinemap;
//
//import java.text.NumberFormat;
//import java.util.ArrayList;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnCancelListener;
//import android.location.Location;
//import android.location.LocationListener;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.baidu.mapapi.BMapManager;
//import com.baidu.mapapi.CoordinateConvert;
//import com.baidu.mapapi.GeoPoint;
//import com.baidu.mapapi.MKAddrInfo;
//import com.baidu.mapapi.MKDrivingRouteResult;
//import com.baidu.mapapi.MKOLSearchRecord;
//import com.baidu.mapapi.MKOLUpdateElement;
//import com.baidu.mapapi.MKOfflineMap;
//import com.baidu.mapapi.MKOfflineMapListener;
//import com.baidu.mapapi.MKPoiResult;
//import com.baidu.mapapi.MKSearch;
//import com.baidu.mapapi.MKSearchListener;
//import com.baidu.mapapi.MKTransitRouteResult;
//import com.baidu.mapapi.MKWalkingRouteResult;
//import com.baidu.mapapi.MapActivity;
//import com.yecon.android.dvr.DRVApp;
//import com.yecon.android.dvr.R;
//import com.yecon.android.dvr.bean.City;
//import com.yecon.android.dvr.bean.Province;
//import com.yecon.android.dvr.file.XMLHelper;
//import com.yecon.android.dvr.gps.MyNetWorkLocationProvider;
//import com.yecon.android.dvr.util.NetWorkUtil;
//
///**
// * 离线地图<BR>
// * [功能详细描述]
// * 
// * @author sunshine
// * @version [yecon Android_Platform, 6 Mar 2012]
// */
//public class OfflineMapActivity extends MapActivity implements MKOfflineMapListener
//{
//
//    private static final int GET_CURRENT_CITY_SUCCESS = 0;
//
//    private static final int DOWNLOAD_INFO_UPDATE = 1;
//
//    private static final int INIT_MAP_SUCCESS = 2;
//
//    private static final int NETWORK_DISABLE = 3;
//
//    private static final int DIALOG_EXIST_DOWNLOADLIST = 0;
//
//    private static final int DIALOG_SUCCESS_DOWNLOADLIST = 1;
//
//    private static final int DIALOG_PROGRESS_LOADING = 2;
//
//    private static final int DIALOG_DELETE_INQUIRY = 3;
//
//    private static final String MB = "MB";
//
//    private MKSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使�?//
//    private ArrayList<Province> cityInfos = null;
//
//    private MKOfflineMap mOffline = null;
//
//    private LinearLayout listContainer = null;
//
//    /**
//     * 当前城市编号
//     */
//    private City currentCity = null;
//
//    private boolean expander = false;
//
//    private Province curShowprovince = null;
//
//    private ArrayList<City> downloadingCitys = new ArrayList<City>();
//
//    private LayoutInflater layoutInflater = null;
//
//    private LinearLayout.LayoutParams params = null;
//
//    private LinearLayout downloadContainer = null;
//
//    private DRVApp app = null;
//
//    private boolean finish = false;
//
//    private City deleteCity = null;
//
//    private OnClickListener clickListener = new OnClickListener()
//    {
//        @Override
//        public void onClick(View v)
//        {
//            switch (v.getId())
//            {
//                // 展示�?��省份的城市列�?//                case R.id.offline_province_button:
//                    doExpander((Integer) v.getTag());
//                    break;
//                // 点击下载操作按钮
//                case R.id.offline_city_button:
//                    downloadClick((Integer) v.getTag());
//                    break;
//                // 删除
//                case R.id.offline_map_download_delete:
//                    doDeleteClick((Integer) v.getTag());
//                    break;
//                // 下载或暂停按�?//                case R.id.offline_map_download_operation:
//                    operation((Integer) v.getTag());
//                    break;
//            }
//
//        }
//    };
//
//    private Handler mHandler = new Handler()
//    {
//        @Override
//        public void handleMessage(Message msg)
//        {
//            switch (msg.what)
//            {
//                case GET_CURRENT_CITY_SUCCESS:
//                    showCurrentCity(currentCity);
//                    break;
//                case DOWNLOAD_INFO_UPDATE:
//                    updateCityInfo(msg.arg1);
//                    break;
//                case INIT_MAP_SUCCESS:
//                    onInitMapSuccess();
//                    break;
//                case NETWORK_DISABLE:
//                    onNetWorkDisable();
//                    break;
//            }
//        }
//    };
//
//    private Runnable initThread = new Runnable()
//    {
//        @Override
//        public void run()
//        {
//            initMap();
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        this.setContentView(R.layout.offline_map);
//
//        app = (DRVApp) this.getApplication();
//        initView();
//        BMapManager bmm = app.getBMapManager();
//        if (null == bmm)
//        {
//            bmm = new BMapManager(getApplication());
//            app.setBMapManager(bmm);
//            app.getBMapManager().init(app.getKey(), new DRVApp.MyGeneralListener());
//        }
//        super.initMapActivity(app.getBMapManager());
//        // 如果使用地图SDK，请初始化地图Activity
//        // super.initMapActivity(app.getBMapManager());
//
//        showDialog(DIALOG_PROGRESS_LOADING);
//        // �?��初始�?//        new Thread(initThread).start();
//    }
//
//    @Override
//    protected void onDestroy()
//    {
//        dismissDialog(DIALOG_PROGRESS_LOADING);
//        super.onDestroy();
//    }
//
//    private void initMap()
//    {
//        app.getBMapManager().start();
//        try
//        {
//            Thread.sleep(1000);
//        }
//        catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
//        // 以下都在线程中启�?//        mOffline = new MKOfflineMap();
//        mOffline.init(app.getBMapManager(), this);
//        // mOffline.scan();
//        initSearch();
//        // Geocoder ge =
//        loadCityInfo();
//    }
//
//    @Override
//    protected void onResume()
//    {
//        super.onResume();
//
//    }
//
//    private void initView()
//    {
//        layoutInflater = this.getLayoutInflater();
//        params =
//            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        listContainer = (LinearLayout) this.findViewById(R.id.offline_map_all_container);
//        downloadContainer = (LinearLayout) this.findViewById(R.id.offline_map_download_container);
//    }
//
//    /**
//     * 展开�?��省区城市或关�?//     */
//    private void doExpander(int id)
//    {
//        if (expander)
//        {
//            showAllProvince();
//        }
//        else
//        {
//            showCityListOfProvince(id);
//        }
//    }
//
//    private void downloadClick(int id)
//    {
//        // 已经在下载队�?//        if (isOnDownloadList(id))
//        {
//            showDialog(DIALOG_EXIST_DOWNLOADLIST);
//            return;
//        }
//
//        Log.d("TAG", "find id = " + id);
//        City c = findCityById(id);
//        findStatus(c);
//        if (null == c)
//        {
//            Log.d("TAG", "c == null return");
//            return;
//        }
//        if (null != c.element)
//        {
//            Log.d("TAG", "start id = " + c.id);
//            mOffline.start(c.id);
//        }
//        else
//        {
//            Log.d("TAG", "start id " + c.id);
//            mOffline.start(c.id);
//            showDialog(DIALOG_SUCCESS_DOWNLOADLIST);
//            // 手动调用�?��
//            onGetOfflineMapState(MKOfflineMap.TYPE_DOWNLOAD_UPDATE, c.id);
//        }
//    }
//
//    /**
//     * 更新城市信息
//     * 
//     * @param id
//     */
//    private void updateCityInfo(int id)
//    {
//        MKOLUpdateElement update = mOffline.getUpdateInfo(id);
//
//        City city = findCityById(id);
//        if (null == update && null != city)
//        {
//            // 更新download信息
//            if (null == city.mDownLoadView)
//            {
//                city.state = MKOLUpdateElement.WAITING;
//                downloadingCitys.add(city);
//                View v = downloadingCityToView(city, layoutInflater);
//                downloadContainer.addView(v, params);
//            }
//            Log.d("TAG", "updateCityInfo is null ~~~~" + id);
//        }
//
//        if (null != update && null != city)
//        {
//            city.element = update;
//            city.state = update.status;
//
//            if (null != city.mView)
//            {
//                infoToView(city.mView, city);
//            }
//            // 更新download信息
//            if (null != city.mDownLoadView)
//            {
//                downloadItemInfo(city, city.mDownLoadView);
//            }
//            // 走这个分支说明，是新添加的下�?//            else
//            {
//                View v = downloadingCityToView(city, layoutInflater);
//                downloadContainer.addView(v, params);
//            }
//        }
//    }
//
//    /**
//     * 是否已经在下载队�?//     * 
//     * @param id
//     * @return
//     */
//    private boolean isOnDownloadList(int id)
//    {
//        for (City c : downloadingCitys)
//        {
//            if (c.id == id)
//            {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private void doDeleteClick(int id)
//    {
//        City city = findCityById(id);
//        Log.d("TAG", "delete id = " + id);
//        deleteCity = city;
//        showDialog(DIALOG_DELETE_INQUIRY);
//    }
//
//    /**
//     * 删除指定ID
//     * 
//     * @param id
//     */
//    private void deleteOfflineCity()
//    {
//        City city = deleteCity;
//        if (null != city)
//        {
//            mOffline.remove(city.id);
//            downloadContainer.removeView(city.mDownLoadView);
//            city.mDownLoadView = null;
//            city.state = MKOLUpdateElement.UNDEFINED;
//            city.element = null;
//            downloadingCitys.remove(city);
//            // 如果下载队列�?��都没有了，则不显示标�?//            if (0 == downloadingCitys.size())
//            {
//                listTitleVisible(false);
//            }
//        }
//        removeDialog(DIALOG_DELETE_INQUIRY);
//    }
//
//    /**
//     * 进行某个操作
//     * 
//     * @param id
//     */
//    private void operation(int id)
//    {
//        City city = findCityById(id);
//        if (null != city)
//        {
//            if (city.state == MKOLUpdateElement.DOWNLOADING)
//            {
//                boolean result = mOffline.pause(id);
//                if (result)
//                {
//                    onGetOfflineMapState(MKOfflineMap.TYPE_DOWNLOAD_UPDATE, city.id);
//                }
//            }
//            else
//            {
//                @SuppressWarnings("unused")
//                boolean result = mOffline.start(id);
//            }
//        }
//    }
//
//    /**
//     * 加载已在下载列表中的城市
//     */
//    private void loadDownloadingCity()
//    {
//        downloadingCitys.clear();
//        ArrayList<MKOLUpdateElement> info = mOffline.getAllUpdateInfo();
//        if (null == info)
//        {
//            return;
//        }
//        for (MKOLUpdateElement elements : info)
//        {
//            City city = findCityById(elements.cityID);
//            // findStatus(city);
//            if (null != city)
//            {
//                city.state = elements.status;
//                city.element = elements;
//                downloadingCitys.add(city);
//            }
//        }
//
//        for (City c : downloadingCitys)
//        {
//            View downloadCityView = downloadingCityToView(c, layoutInflater);
//            downloadContainer.addView(downloadCityView, params);
//        }
//
//        Log.d("download size ", "size = " + downloadingCitys.size());
//    }
//
//    private void listTitleVisible(boolean show)
//    {
//        View title = this.findViewById(R.id.offline_map_downloadlist_title);
//        title.setVisibility(show ? View.VISIBLE : View.GONE);
//    }
//
//    /**
//     * 构建正在下载的项�?//     * 
//     * @param city
//     * @param layoutInflater
//     * @return
//     */
//    private View downloadingCityToView(City city, LayoutInflater layoutInflater)
//    {
//        if (null == city)
//        {
//            return null;
//        }
//        listTitleVisible(true);
//        View downloadingCityView = layoutInflater.inflate(R.layout.offline_map_list_download_item, null);
//        downloadItemInfo(city, downloadingCityView);
//        city.mDownLoadView = downloadingCityView;
//        return downloadingCityView;
//    }
//
//    /**
//     * 更新某项信息
//     * 
//     * @param city
//     * @param item
//     */
//    private void downloadItemInfo(City city, View item)
//    {
//        TextView cityNameText = (TextView) item.findViewById(R.id.offline_map_download_city_name);
//        cityNameText.setText(city.name);
//        TextView downloadPercentText = (TextView) item.findViewById(R.id.offline_map_download_percent);
//        downloadPercentText.setText(null == city.element ? "0%" : city.element.ratio + "%");
//        TextView sizeText = (TextView) item.findViewById(R.id.offline_map_download_size);
//        sizeText.setText(sizeToStr(city.size));
//        TextView downloadStatusText = (TextView) item.findViewById(R.id.offline_map_download_status);
//        downloadStatusText.setTag(city.id);
//        ImageButton operatorIcon = (ImageButton) item.findViewById(R.id.offline_map_download_operation);
//        operatorIcon.setTag(city.id);
//        int statusResId = 0;
//        int operatorResId = 0;
//        switch (city.state)
//        {
//            case MKOLUpdateElement.WAITING:
//                statusResId = R.string.offline_map_waiting;
//                operatorResId = R.drawable.offline_map_download_icon;
//                break;
//            case MKOLUpdateElement.DOWNLOADING:
//                statusResId = R.string.offline_map_downloading;
//                operatorResId = R.drawable.offline_map_download_pause;
//                break;
//            case MKOLUpdateElement.FINISHED:
//                statusResId = R.string.offline_map_downloaded;
//                operatorResId = R.drawable.offline_map_disable;
//                break;
//            case MKOLUpdateElement.SUSPENDED:
//                statusResId = R.string.offline_map_pause;
//                operatorResId = R.drawable.offline_map_download_icon;
//                break;
//            case MKOLUpdateElement.UNDEFINED:
//                statusResId = 0;
//                operatorResId = R.drawable.offline_map_download_icon;
//                break;
//            default:
//                statusResId = R.drawable.offline_map_disable;
//                break;
//        }
//        downloadStatusText.setText(statusResId);
//        operatorIcon.setImageResource(operatorResId);
//        ImageButton downloadDelete = (ImageButton) item.findViewById(R.id.offline_map_download_delete);
//        downloadDelete.setTag(city.id);
//        downloadDelete.setOnClickListener(clickListener);
//        operatorIcon.setOnClickListener(clickListener);
//    }
//
//    private void onNetWorkDisable()
//    {
//        app.showToast(this.getString(R.string.offline_map_network_disable));
//        dismissDialog(DIALOG_PROGRESS_LOADING);
//    }
//
//    /**
//     * 加载城市信息
//     */
//    private void loadCityInfo()
//    {
//        // 1.加载本地城市配置�?//        // 2.将本地名字对应的ID查询�?//        // 3.查询本地离线地图是否存在
//        // 4.查询设备此时�?��位置
//        // 5.排列列表，然后展示出�?排列按照直辖市，省份，特区的位置排列
//        cityInfos = XMLHelper.readXML(this, R.xml.all_city);
//        // 找出�?��支持的城�?//        ArrayList<MKOLSearchRecord> allSupportCitys = null;
//        boolean running = true;
//        boolean fail = false;
//        do
//        {
//            allSupportCitys = mOffline.getOfflineCityList();
//            // Log.d("TAG", "support size ++ " + allSupportCitys.size());
//            if (null == allSupportCitys || 0 == allSupportCitys.size())
//            {
//                if (!NetWorkUtil.checkNetWorkEnable(OfflineMapActivity.this))
//                {
//                    fail = true;
//                    running = false;
//                }
//            }
//            else
//            {
//                running = false;
//            }
//            try
//            {
//                Thread.sleep(2000);
//            }
//            catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
//            // 这里有个死循环，�?��等待基本文件下载完成
//        }
//        while (running);
//        // 2
//        if (fail)
//        {
//            mHandler.obtainMessage(NETWORK_DISABLE).sendToTarget();
//        }
//        else
//        {
//            findId(allSupportCitys);
//            Log.d("TAG", "city size = " + cityInfos.size());
//            // 如果没有结束
//            if (!finish)
//            {
//                mHandler.obtainMessage(INIT_MAP_SUCCESS).sendToTarget();
//            }
//        }
//    }
//
//    private void onInitMapSuccess()
//    {
//        // 3 这个动作是异步的 ,等当前城市找到后在在当前城市的项中设置当前城�?//        getCurrentCity();
//        // 查找已有的下载项
//        loadDownloadingCity();
//        listToView();
//        dismissDialog(DIALOG_PROGRESS_LOADING);
//    }
//
//    private void exit()
//    {
//        finish = true;
//        this.finish();
//    }
//
//    /**
//     * 显示数据出来
//     */
//    private void listToView()
//    {
//        for (int i = 0; i < cityInfos.size(); i++)
//        {
//            Province province = cityInfos.get(i);
//            // 如果是直辖市
//            if (0 == province.citys.size())
//            {
//                // listContainer.addView(cityToView(province, layoutInflater), params);
//                continue;
//            }
//            // 如果不是直辖�?//            else
//            {
//                View provinceView = layoutInflater.inflate(R.layout.offline_map_list_province_item, null);
//                ((TextView) provinceView.findViewById(R.id.offline_province_name)).setText(province.name);
//                listContainer.addView(provinceView, params);
//
//                ImageButton exButton = (ImageButton) provinceView.findViewById(R.id.offline_province_button);
//                exButton.setOnClickListener(clickListener);
//                exButton.setTag(i);
//            }
//        }
//    }
//
//    /**
//     * 显示�?��省份的所有城�?//     * 
//     * @param i
//     */
//    private void showCityListOfProvince(int i)
//    {
//        Province province = cityInfos.get(i);
//        curShowprovince = province;
//
//        LinearLayout subContainer = (LinearLayout) findViewById(R.id.offline_map_all_container_sub);
//        subContainer.removeAllViews();
//
//        // 先把省份弄上�?//        View provinceView = layoutInflater.inflate(R.layout.offline_map_list_province_item, null);
//        ((TextView) provinceView.findViewById(R.id.offline_province_name)).setText(province.name);
//        ImageButton expanderBtn = (ImageButton) provinceView.findViewById(R.id.offline_province_button);
//        expanderBtn.setImageResource(R.drawable.offline_map_expander_ic_maximized);
//        expanderBtn.setOnClickListener(clickListener);
//        expanderBtn.setTag(i);
//        subContainer.addView(provinceView, params);
//
//        for (int n = 0; n < province.citys.size(); n++)
//        {
//            City city = province.citys.get(n);
//            subContainer.addView(cityToView(city, layoutInflater), params);
//        }
//        this.findViewById(R.id.offline_map_all).setVisibility(View.GONE);
//        this.findViewById(R.id.offline_map_sub).setVisibility(View.VISIBLE);
//
//        expander = true;
//    }
//
//    /**
//     * 显示省份
//     */
//    private void showAllProvince()
//    {
//        expander = false;
//        View subScrollView = findViewById(R.id.offline_map_sub);
//        subScrollView.setVisibility(View.GONE);
//        this.findViewById(R.id.offline_map_all).setVisibility(View.VISIBLE);
//        for (int j = 0; j < curShowprovince.citys.size(); j++)
//        {
//            City city = curShowprovince.citys.get(j);
//            city.mView = null;
//        }
//    }
//
//    /**
//     * 将城市转化为view展示
//     * 
//     * @param city
//     * @param layoutInflater
//     * @return
//     */
//    private View cityToView(City city, LayoutInflater layoutInflater)
//    {
//        View cityView = layoutInflater.inflate(R.layout.offline_map_list_city_item, null);
//        infoToView(cityView, city);
//        return cityView;
//    }
//
//    private void infoToView(View cityView, City city)
//    {
//        cityView.setTag(city.id);
//        TextView cityNameView = ((TextView) cityView.findViewById(R.id.offline_city_name));
//        TextView sizeView = (TextView) cityView.findViewById(R.id.offline_city_size);
//        cityNameView.setText(city.name);
//        sizeView.setText(sizeToStr(city.size));
//        sizeView.setVisibility(View.VISIBLE);
//        ImageButton downLoadBtn = (ImageButton) cityView.findViewById(R.id.offline_city_button);
//        downLoadBtn.setVisibility(View.VISIBLE);
//        downLoadBtn.setTag(city.id);
//        city.mView = cityView;
//        downLoadBtn.setOnClickListener(clickListener);
//    }
//
//    /**
//     * 字节转换为xxx.xxMB
//     * 
//     * @param size
//     * @return
//     */
//    private String sizeToStr(int size)
//    {
//        double mbSize = size / 1024d / 1024;
//        NumberFormat bnf = NumberFormat.getInstance();
//        bnf.setMinimumFractionDigits(2);
//        bnf.setMaximumFractionDigits(2);
//        return bnf.format(mbSize) + MB;
//    }
//
//    /**
//     * 展示当前城市
//     * 
//     * @param city
//     */
//    private void showCurrentCity(City city)
//    {
//        View current = this.findViewById(R.id.offline_current_city);
//        infoToView(current, city);
//    }
//
//    /**
//     * 查找某个城市目前对应的状�?//     * 
//     * @param city
//     */
//    private void findStatus(City city)
//    {
//        MKOLUpdateElement myElement = mOffline.getUpdateInfo(city.id);
//        if (null != myElement)
//        {
//            city.element = myElement;
//            city.state = myElement.status;
//        }
//    }
//
//    /**
//     * 查找ID
//     */
//    private void findId(ArrayList<MKOLSearchRecord> supportCitys)
//    {
//        // 没SD�?这里会出问题，所有要作处�?//        for (int i = 0; i < cityInfos.size(); i++)
//        {
//            Province province = cityInfos.get(i);
//            // 如果是直辖市
//            if (0 == province.citys.size())
//            {
//                ArrayList<MKOLSearchRecord> records = mOffline.searchCity(province.name);
//                if (records != null && records.size() == 1)
//                {
//                    province.id = records.get(0).cityID;
//                    Log.d("TAG", "city id ====" + province.id);
//                    province.size = records.get(0).size;
//                    province.name = records.get(0).cityName;
//                    findStatus(province);
//
//                }
//                // 要从列表中去�?//                else
//                {
//                    cityInfos.remove(i);
//                    i--;
//                }
//            }
//            // 如果不是直辖�?//            else
//            {
//                for (int j = 0; j < province.citys.size(); j++)
//                {
//                    City city = province.citys.get(j);
//                    MKOLSearchRecord matcher = null;
//                    if (null != supportCitys)
//                    {
//                        for (MKOLSearchRecord record : supportCitys)
//                        {
//                            if (record.cityName.equals(city.name) || record.cityName.contains(city.name))
//                            {
//                                matcher = record;
//                                break;
//                            }
//                        }
//                    }
//                    if (matcher != null)
//                    {
//
//                        city.id = matcher.cityID;
//                        city.name = matcher.cityName;
//                        city.size = matcher.size;
//                        findStatus(city);
//                    }
//                    // 要从列表中去�?//                    else
//                    {
//                        province.citys.remove(j);
//                        j--;
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 获取当前城市
//     * 
//     * @return
//     */
//    private void getCurrentCity()
//    {
//        final MyNetWorkLocationProvider netProvider = new MyNetWorkLocationProvider(this);
//        netProvider.getMyLocation(new LocationListener()
//        {
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras)
//            {
//            }
//
//            @Override
//            public void onProviderEnabled(String provider)
//            {
//            }
//
//            @Override
//            public void onProviderDisabled(String provider)
//            {
//            }
//
//            @Override
//            public void onLocationChanged(Location location)
//            {
//                GeoPoint p =
//                    new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
//                Bundle b = CoordinateConvert.fromWgs84ToBaidu(p);
//                GeoPoint convertedPoint = CoordinateConvert.bundleDecode(b);
//                mSearch.reverseGeocode(convertedPoint);
//                // 获取到位置就移除
//                netProvider.close();
//            }
//        });
//    }
//
//    private City searchCurrentCity(String name)
//    {
//        for (int i = 0; i < cityInfos.size(); i++)
//        {
//            Province province = cityInfos.get(i);
//            for (int j = 0; j < province.citys.size(); j++)
//            {
//                if (name.equals(province.citys.get(j).name))
//                {
//                    return province.citys.get(j);
//                }
//            }
//        }
//        return null;
//    }
//
//    @Override
//    protected void onStop()
//    {
//        DRVApp app = (DRVApp) this.getApplication();
//        BMapManager bmm = app.getBMapManager();
//        bmm.stop();
//        super.onStop();
//    }
//
//    private City findCityById(int id)
//    {
//        for (int i = 0; i < cityInfos.size(); i++)
//        {
//            Province province = cityInfos.get(i);
//            if (0 == province.citys.size())
//            {
//                if (province.id == id)
//                {
//                    return province;
//                }
//            }
//            else
//            {
//                for (int j = 0; j < province.citys.size(); j++)
//                {
//                    City city = province.citys.get(j);
//                    if (city.id == id)
//                    {
//                        return city;
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 初始化搜�?//     */
//    private void initSearch()
//    {
//        // 初始化搜索模块，注册事件监听
//        mSearch = new MKSearch();
//        DRVApp app = (DRVApp) this.getApplication();
//        mSearch.init(app.getBMapManager(), new MKSearchListener()
//        {
//            public void onGetAddrResult(MKAddrInfo res, int error)
//            {
//                Log.d("TAG", "onGetAddrResult");
//                if (error != 0)
//                {
//                    String str = String.format("错误号：%d", error);
//                    Toast.makeText(OfflineMapActivity.this, str, Toast.LENGTH_LONG).show();
//                    return;
//                }
//                // 获取成功�?//                else
//                {
//                    Log.d("TAG", "success to get current city = " + res.addressComponents.city);
//                    City city = searchCurrentCity(res.addressComponents.city);
//                    if (null != city)
//                    {
//                        ArrayList<MKOLSearchRecord> records = mOffline.searchCity(city.name);
//                        if (records != null && records.size() == 1)
//                        {
//                            currentCity = city;
//                            currentCity.size = records.get(0).size;
//                            Log.d("TAG", "city id = " + records.get(0).cityID);
//                            currentCity.id = records.get(0).cityID;
//                            mHandler.sendEmptyMessage(GET_CURRENT_CITY_SUCCESS);
//                        }
//                    }
//                }
//            }
//
//            public void onGetPoiResult(MKPoiResult res, int type, int error)
//            {
//            }
//
//            public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error)
//            {
//            }
//
//            public void onGetTransitRouteResult(MKTransitRouteResult res, int error)
//            {
//            }
//
//            public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error)
//            {
//            }
//
//        });
//    }
//
//    @Override
//    public void onGetOfflineMapState(int type, int state)
//    {
//        Log.d("TAG", "type = " + type + " state = " + state);
//        switch (type)
//        {
//            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
//                Message msg = mHandler.obtainMessage(DOWNLOAD_INFO_UPDATE);
//                msg.arg1 = state;
//                msg.sendToTarget();
//                break;
//            case MKOfflineMap.TYPE_NEW_OFFLINE:
//                break;
//            case MKOfflineMap.TYPE_VER_UPDATE:
//                break;
//        }
//    }
//
//    @Override
//    protected boolean isRouteDisplayed()
//    {
//        // TODO Auto-generated method stub
//        return false;
//    }
//    
//    protected Dialog onCreateDialog(int id)
//    {
//        switch (id)
//        {
//            case DIALOG_EXIST_DOWNLOADLIST:
//                return new AlertDialog.Builder(this).setIcon(R.drawable.dialog_icon_warring)
//                    .setTitle(R.string.wariness)
//                    .setMessage(R.string.offline_map_exist_tip)
//                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
//                    {
//                        public void onClick(DialogInterface dialog, int whichButton)
//                        {
//
//                        }
//                    })
//
//                    .create();
//            case DIALOG_SUCCESS_DOWNLOADLIST:
//                return new AlertDialog.Builder(this).setIcon(R.drawable.dialog_icon_warring)
//                    .setTitle(R.string.wariness)
//                    .setMessage(R.string.offline_map_success_tip)
//                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
//                    {
//                        public void onClick(DialogInterface dialog, int whichButton)
//                        {
//
//                        }
//                    })
//
//                    .create();
//            case DIALOG_PROGRESS_LOADING:
//                ProgressDialog dialog = new ProgressDialog(this);
//                dialog.setMessage(getString(R.string.offline_map_loading));
//                dialog.setIndeterminate(true);
//                dialog.setCancelable(true);
//                dialog.setOnCancelListener(new OnCancelListener()
//                {
//                    @Override
//                    public void onCancel(DialogInterface dialog)
//                    {
//                        exit();
//                    }
//                });
//                return dialog;
//                // 离线地图删除对话�?//            case DIALOG_DELETE_INQUIRY:
//                return new AlertDialog.Builder(this).setIcon(R.drawable.dialog_icon_warring)
//                    .setTitle(R.string.wariness)
//                     .setMessage(String.format(OfflineMapActivity.this.getString(R.string.offline_map_delete), deleteCity.name))
//                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
//                    {
//                        public void onClick(DialogInterface dialog, int whichButton)
//                        {
//                            deleteOfflineCity();
//                        }
//                    })
//                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
//                    {
//                        public void onClick(DialogInterface dialog, int whichButton)
//                        {
//
//                        }
//                    })
//                    .create();
//            default:
//                return null;
//        }
//    }
//}
