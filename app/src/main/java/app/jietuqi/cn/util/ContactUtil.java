package app.jietuqi.cn.util;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.text.TextUtils;

import java.util.ArrayList;

import app.jietuqi.cn.constant.SharedPreferenceKey;
import app.jietuqi.cn.ui.entity.ContactEntity;

/**
 * 作者： liuyuanbo on 2018/11/25 21:24.
 * 时间： 2018/11/25 21:24
 * 邮箱： 972383753@qq.com
 * 用途： 联系人的操作工具类
 */
public class ContactUtil {

    /**
     * 批量删除联系人
     */
    public static void batchDelContact(Context context) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        ArrayList<ContactEntity> list = UserOperateUtil.getContactList();
        for (int i = 0, size = list.size(); i < size; i++) {
            ContactEntity model = UserOperateUtil.getContactList().get(i);
            ops.add(ContentProviderOperation.newDelete(ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, model.rawContactId))
                    .withYieldAllowed(true)
                    .build());
        }
        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            UserOperateUtil.deleteContactCache(context);
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定联系人
     * @param context
     * @param entity
     */
    public static void deleteOneContact(Context context, ContactEntity entity){
        ContentResolver cr = context.getContentResolver();

        //第一步先删除Contacts表中的数据
        cr.delete(ContactsContract.Contacts.CONTENT_URI, ContactsContract.Contacts._ID + " =?", new String[]{entity.rawContactId+""});
        //第二步再删除RawContacts表的数据
        cr.delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts.CONTACT_ID + " =?", new String[]{entity.rawContactId+""});
    }

    /**
     * 批量增加联系人
     * @param context
     * @param contactList
     * @return
     */
    public static boolean batchAddContact(Context context, ArrayList<ContactEntity> contactList) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        int rawContactInsertIndex;
        if (contactList == null || contactList.size() == 0) {
            return false;
        }
        for (int i = 0; i < contactList.size(); i++) {
            rawContactInsertIndex = ops.size();
            ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .withYieldAllowed(true)
                    .build());
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, "yxb_"+contactList.get(i).nickName)
                    .withYieldAllowed(true)
                    .build());
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contactList.get(i).phoneNumner)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .withYieldAllowed(true)
                    .build());
        }
        try {
            //这里才调用的批量添加
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                name = StringUtils.insertFront(name, "yxb_");
                for (ContactEntity model : contactList) {
//                    String nameRemoveYXB = name.replace("yxb_", "");
                    if (("yxb_" + model.nickName).equals(name)) {
                        model.rawContactId = contactId;
                    }
                }
            }
            ArrayList<ContactEntity> list = UserOperateUtil.getContactList();
            list.addAll(contactList);
            SharedPreferencesUtils.putListData(SharedPreferenceKey.CONTACT, list);
            if (cursor != null) {
                cursor.close();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        } catch (OperationApplicationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void addOneContact(Context context, ContactEntity entity){
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentValues values = new ContentValues();
        ContentResolver contentResolver = context.getContentResolver();
        long rawContactId = ContentUris.parseId(contentResolver.insert(uri, values));
        //插入data表
        uri = Uri.parse("content://com.android.contacts/data");
        // 向data表插入数据
        if (!TextUtils.isEmpty(entity.nickName)) {
            values.put("raw_contact_id", rawContactId);
            values.put("mimetype", "vnd.android.cursor.item/name");
            values.put("data2", StringUtils.insertFront(entity.nickName, "yxb_"));
            contentResolver.insert(uri, values);
        }
        // 向data表插入电话号码
        if (!TextUtils.isEmpty(entity.phoneNumner)) {
            values.clear();
            values.put("raw_contact_id", rawContactId);
            values.put("mimetype", "vnd.android.cursor.item/phone_v2");
            values.put("data2", "2");
            values.put("data1", entity.phoneNumner);
            contentResolver.insert(uri, values);
        }
        ArrayList<ContactEntity> list = UserOperateUtil.getContactList();
        entity.rawContactId = rawContactId;
        list.add(entity);
        SharedPreferencesUtils.putListData(SharedPreferenceKey.CONTACT, list);
    }
}
