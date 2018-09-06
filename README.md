# Carousel [![](https://jitpack.io/v/amikoj/Carousel.svg)](https://jitpack.io/#amikoj/Carousel)

轮播广告控件，实现简单的广告循环轮播功能

### 使用方法

1. 添加依赖

项目根目录的build.gradle
```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

需要使用的工程的配置build.gradle
```groovy

dependencies {
	        implementation 'com.github.amikoj:Carousel:1.2.1'
		implementation 'com.github.bumptech.glide:glide:4.7.1'
	}

```


2. 使用

```xml

  <cn.enjoyoday.carousel.CarouselLayout
                android:id="@+id/carousel_layout"
                android:layout_width="match_parent"
                android:layout_height="150dp"
                android:background="@drawable/lottery_bg"
                app:layout_constraintStart_toStartOf="parent"
                app:loadFailed="@drawable/lottery_bg"
                app:preLoad="@drawable/lottery_bg"
                app:urls="@array/ad_urls"
                app:type="ONLINE" />

<!-- app:loadFailed 图片加载失败显示图片-->
<!-- app:preLoad　图片加载成功之前显示图片-->
<!-- app:type  加载图片方式,ONLINE:在线方式，LOCAL：本地资源-->
<!-- app:urls 在线加载图片的url,需要type为ONLINE才能生效-->
<!--  app:resIds 加载本地图片array类型，需要type为LOCAL才能生效-->

```

type:使用数据类型,LOCAL(本地资源),ONLINE(在线资源)
urls:在线图片的链接数组
resIds:本地图片id的数组

3. 代码设置
可以通过代码设置：
```
 carouselLayout.setUrls(urls);
                        carouselLayout.setOnItemClick(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                              // 点击回调
                            }
                        });
```
