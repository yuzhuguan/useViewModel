# useViewModel
老项目，单独使用ViewModel

![](screenshot.png)

## 思考

Good news
> View的部分的代码变得十分简单，代码维护比较容易
> 使用ViewModel的关键好处之一是数据不与视图的生命周期（Activity）绑定;
例如，如果旋转设备，Activity将被销毁并重新创建，但ViewModel将一直存在，直到Activity finish
> 小技巧，Activity从Viewmodel获取的data 都是当时ViewModel里数据的副本，就是说Activity跟数据彻底分离

Bad news
> 数据库部分代码仍需要大量的code 我知道Room能很好的解决
> 一旦数据有变化，需要分别通知viewmodel及adapter， 我知道livedata能解决这个问题
