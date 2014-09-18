/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\GitHub\\SmallTiger\\Tiger\\src\\com\\mn\\tiger\\service\\TGRemoteService.aidl
 */
package com.mn.tiger.service;
public interface TGRemoteService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.mn.tiger.service.TGRemoteService
{
private static final java.lang.String DESCRIPTOR = "com.mn.tiger.service.TGRemoteService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.mn.tiger.service.TGRemoteService interface,
 * generating a proxy if needed.
 */
public static com.mn.tiger.service.TGRemoteService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.mn.tiger.service.TGRemoteService))) {
return ((com.mn.tiger.service.TGRemoteService)iin);
}
return new com.mn.tiger.service.TGRemoteService.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_invoke:
{
data.enforceInterface(DESCRIPTOR);
com.mn.tiger.task.invoke.TGTaskParams _arg0;
if ((0!=data.readInt())) {
_arg0 = com.mn.tiger.task.invoke.TGTaskParams.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.invoke(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.mn.tiger.service.TGRemoteService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void invoke(com.mn.tiger.task.invoke.TGTaskParams taskParams) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((taskParams!=null)) {
_data.writeInt(1);
taskParams.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_invoke, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_invoke = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void invoke(com.mn.tiger.task.invoke.TGTaskParams taskParams) throws android.os.RemoteException;
}
