package com.ccj.client.android.analytics;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ccj.client.android.analytics.bean.EventBean;
import com.ccj.client.android.analytics.db.EFinalDb;

import java.util.List;


/**
 * 处理多线程 读写的时候,想要线程安全.需要:
 * 1.所有线程共用一个数据库连接,也就是一个db 连接器DBHelper 否则会出现数据库锁
 * 2.多线程写， 使用同一个SQLiteOpenHelpe, 并且对数据库操作方法使用synchronized 就不会出问题

 *
 * Created by chenchangjun on 18/2/8.
 */
 class EDBHelper {

    private static final EFinalDb db;

   // SQLite 采用串行模型，所有线程都公用同一个数据库连接。
    static {
        db = EFinalDb.create(JJEventManager.getContext(), EConstant.DB_NAME, false, EConstant.DB_VERSION,
                new EFinalDb.DbUpdateListener() {
                    @Override
                    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                        Cursor cursor = db.rawQuery(
                                "SELECT name FROM sqlite_master WHERE type ='table' AND name != 'sqlite_sequence'", null);
                        if (cursor != null) {
                            while (cursor.moveToNext()) {
                                db.execSQL("DROP TABLE " + cursor.getString(0));
                            }
                        }
                        if (cursor != null) {
                            cursor.close();
                            cursor = null;
                        }
                        ELogger.logWrite(EConstant.TAG, "onUpgrade ,delete DB_NAME success!-->");

                    }
                });
    }


    /**
     *  检索 该时间节点之前的数据
     * @param formateDate
     * @return
     */
    public static synchronized List<EventBean> getEventListByDate(String formateDate) {
        //select * from shopping.tb_item where it < '2015-03-08 21:28:44';
        List<EventBean> resultList = null;
        try {
            resultList = db.findAllByWhere(EventBean.class, " it<\"" + formateDate + "\"");
            ELogger.logWrite(EConstant.TAG, "getEventListByDate  success!-->"+formateDate+"--resultList.size()--"+resultList.size());

        } catch (Exception e) {
            e.printStackTrace();
            ELogger.logWrite(EConstant.TAG, "getEventListByDate  failed-->"+e.getMessage());

        }

        return resultList;

    }


    /**
     * 获取 检索记录行 start-end 之间的数据
     * @return
     */
    public static synchronized List<EventBean> getEventListByLimit(int  start ,int end){
        //mysql> SELECT * FROM table LIMIT 5,10;  // 检索记录行 6-15
        //mysql> SELECT * FROM table LIMIT 95,-1; // 检索记录行 96-last.
        //mysql> SELECT * FROM table LIMIT 5;     //检索前 5 个记录行
        List<EventBean> resultList = null;
        try {
            resultList = db.findAllByLimit(EventBean.class, start,end);
            ELogger.logWrite(EConstant.TAG, "getEventListByLimit  success!-->第"+start+"条到"+end+"之间--resultList.size()--"+resultList.size());

        } catch (Exception e) {
            e.printStackTrace();
            ELogger.logWrite(EConstant.TAG, "getEventListByLimit  failed-->"+e.getMessage());

        }

        return resultList;
    }


    /**
     * 获取 检索记录行 start-end 之间的数据
     * @return
     */
    public static synchronized void deleteEventListByLimit(int  start ,int end){

        List<EventBean> resultList = null;
        try {
             db.deleteByLimit(EventBean.class, start,end);
            ELogger.logWrite(EConstant.TAG, "deleteEventListByLimit  success!-->第"+start+"条到"+end+"之间--resultList.size()--"+resultList.size());

        } catch (Exception e) {
            e.printStackTrace();
            ELogger.logWrite(EConstant.TAG, "deleteEventListByLimit  failed-->"+e.getMessage());

        }
    }




    /**
     *  检索 数据库的条数
     * @return
     */
    public static synchronized int  getEventRowCount() {
        //select * from shopping.tb_item where it < '2015-03-08 21:28:44';
        int resultCount = 0;
        try {
            resultCount = db.getRowCount(EventBean.class);
            ELogger.logWrite(EConstant.TAG, "getEventRowCount  success!-->"+resultCount);

        } catch (Exception e) {
            e.printStackTrace();
            ELogger.logWrite(EConstant.TAG, "getEventRowCount  failed-->"+e.getMessage());
        }

        return resultCount;

    }


    /**
     * 删除该时间节点之前的数据
     * @param formateDate
     */
    public static synchronized void deleteEventListByDate(String formateDate) {
        //select * from shopping.tb_item where it < '2015-03-08 21:28:44';
        try {
            db.deleteByWhere(EventBean.class, " it<\"" + formateDate + "\"");
            ELogger.logWrite(EConstant.TAG, "deleteEventListByDate  success!-->"+formateDate);

        } catch (Exception e) {
            e.printStackTrace();
            ELogger.logWrite(EConstant.TAG, "deleteEventListByDate  failed-->"+e.getMessage());
        }


    }


    /**
     * 向数据库中添加一条记录
     *
     * @param data
     */
    public static synchronized boolean addEventData(EventBean data) {
        try {
            // 如果该条数据存在于数据库中 删掉原来的记录 添加新纪录 保持在最新浏览位置
            db.save(data);

            ELogger.logWrite(EConstant.TAG, "save to db success-->"+data.toString());

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            ELogger.logError(EConstant.TAG, "save to db failed-->"+e.getMessage());
            if (e.getMessage().contains("no column named")){
                db.dropTable(EventBean.class);
                //table eventlist has no column named ds
                ELogger.logWrite(EConstant.TAG, "has no column named,so dropTable");
                db.save(data);
                ELogger.logWrite(EConstant.TAG, "reload : save to db success-->");

            }
            return false;
        }
    }





    /**
     * 删除所有统计事件
     *
     * @return
     */
    @Deprecated
    public static boolean clearAllCache() {
        try {
           // db.deleteAll(EventBean.class);
            return true;
        } catch (Exception e) {
            ELogger.logWrite(EConstant.TAG, "-clearAllCache:" + e.toString());
        }
        return false;
    }































    /**
     * 向数据库中添加一条记录
     *
     * @param data
     */
    @Deprecated
    public static synchronized boolean addData(EventBean data) {
        try {
            // 如果该条数据存在于数据库中 删掉原来的记录 添加新纪录 保持在最新浏览位置
            if (null != findData("" + data.getId())) {
                updateData(data);
            } else {
                db.save(data);
            }
            return true;
        } catch (Exception e) {

            ELogger.logWrite(EConstant.TAG, "-addListCache" + e.toString());
            return false;
        }
    }

    /**
     * 根据主键id删除该条数据记录
     *
     * @param id 需要更新的主键索引值
     * @return 成功操作返回true 不成功返回false
     */
    public static boolean deleteData(String id) {
        try {
            db.deleteById(EventBean.class, id);
            return true;
        } catch (Exception e) {
            ELogger.logWrite(EConstant.TAG, "-deletelistCache" + e.toString());
            return false;
        }
    }


    /**
     * 根据主键id更新该条数据信息
     *
     * @param data 数据存储处理对象
     * @return 成功操作返回true 不成功返回false
     */
    public static synchronized boolean updateData(EventBean data) {
        try {
            db.update(data, "id = " + data.getId());
            return true;
        } catch (Exception e) {
            ELogger.logWrite(EConstant.TAG, "-updateRowInfoById" + e.toString());
            return false;
        }
    }

    /**
     * 根据主键id查询数据库中该条记录
     *
     * @param id 需要更新的主键索引值
     */
    public static synchronized EventBean findData(String id) {
        try {
            return db.findById(id, EventBean.class);
        } catch (Exception e) {
            ELogger.logWrite(EConstant.TAG, "-findRowInfoById" + e.toString());
            return null;
        }
    }






    /**
     *  检索 是否存在记录
     * @param ExposedID
     * @return
     */
    public static synchronized List<EventBean> getEventByExposedID(String ExposedID) {
        //select * from shopping.tb_item where it < '2015-03-08 21:28:44';
        List<EventBean> resultList = null;
        try {
            resultList= db.findAllByWhere(EventBean.class, " exposed_id=\"" + ExposedID + "\"");
//
//            db.deleteByWhere(EventBean.class, " exposed_id=\"" + ExposedID + "\"");
            ELogger.logWrite(EConstant.TAG, "getEventByExposedID  success!-->"+ExposedID+"--resultList.size()--"+resultList.size());

        } catch (Exception e) {
            e.printStackTrace();
            ELogger.logWrite(EConstant.TAG, "getEventByExposedID  failed-->"+e.getMessage());
            if (e.getMessage().contains("no column named")){
                db.dropTable(EventBean.class);
                //table eventlist has no column named ds
                ELogger.logWrite(EConstant.TAG, "has no column named,so dropTable");
                try {
                    resultList= db.findAllByWhere(EventBean.class, " exposed_id=\"" + ExposedID + "\"");
                    ELogger.logWrite(EConstant.TAG, "getEventByExposedID  success!-->"+ExposedID+"--resultList.size()--"+resultList.size());

                } catch (Exception e1) {
                    e1.printStackTrace();
                    ELogger.logWrite(EConstant.TAG, "getEventByExposedID  failed-->"+e.getMessage());

                }
                return resultList;

            }
        }

        return resultList;

    }


    public static synchronized boolean updateEventBean(EventBean bean, String ExposedID) {
        try {
            db.update(bean, " exposed_id=\"" + ExposedID + "\"");
            ELogger.logWrite(EConstant.TAG, "-updateEventBean  sucess ,exposed_id=" + ExposedID);

            return true;
        } catch (Exception e) {
            ELogger.logWrite(EConstant.TAG, "-updateRowInfoBy exposed_id" + e.toString());
            return false;
        }
    }



}
