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
	        implementation 'com.github.amikoj:Carousel:1.2'
	}

```


2. 使用

```xml

    <cn.enjoyoday.cn.carousel.CarouselLayout
        android:layout_width="match_parent"
        app:type="ONLINE"
        app:urls="@array/ad_urls"
        android:layout_height="150dp"/>

```

type:使用数据类型,LOCAL(本地资源),ONLINE(在线资源)
urls:在线图片的链接数组
resIds:本地图片id的数组