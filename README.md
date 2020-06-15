# NagoyaHotel

## Setup

### create keyfinger

```
keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass your_app_password
```
and copy the sha1 key and update

```
https://console.developers.google.com/flows/enableapi?apiid=maps_android_backend&keyType=CLIENT_SIDE_ANDROID&r={your_sha1_key}%3Bcom.quoctran.nagoyalhotel
```
Then create a key on google console with owner role user.

Copy and pass into `google_maps_api.xml` into `bug` and `release` in
```
<string name="google_maps_key" templateMergeStrategy="preserve" translatable="false">{your_sha1_code}</string>
```
## Run and taste