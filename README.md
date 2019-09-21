# ImageLoader Library

This Library is used for Image caching in two layer, In Memory and In Disk Cache.


## Usage

You can use it like:
ImageLoader imageLoader=ImageLoader.get(context);
imageLoader.displayImage(imageUrl, imageView);

Note: ImageUrl, ImageView and Context should not be null.


## Sample Project

Sample project is a Basic Recipe List project, Where you can search the recipe and also browse through category.

## Used Architecture
Sample project - MVVM
ImageLoader Library - SOLID Principle. 

# Third party library used

Sample Project - CircleImageView, Retrofit
ImageLoader Library - Jake Wharton Disk LRU Cache

# API Used in sample Project

https://www.food2fork.com
