# 对 gourds 分支代码的理解

## 1. 关于 Tile

根据 `Tile` 的类定义及相关方法，`Tile` 中的 `T thing` 成员是一个 `Thing` 的子类的对象，所以 `Tile` 应该是一个可以用来放任何是 `Thing` 的内容的一个位置，有一些坐标的属性，相当于给一个 `Thing` 固定了一个坐标。

## 2. 关于 Tile<? extends Thing> tile

`Tile` 是用来给 `Thing` 固定坐标的，所以 `Thing` 的坐标应该就是自己所属的 `Tile` 的坐标，所以应该有一个成员来记录所属的 `Tile` 。因为 `Thing` 是存在各种子类的，在父类的定义中应该让自己的 `Tile` 属性支持自己的各种子类，所以要是 `Tile<? extends Thing>`，这里表示一个可以放置任意一种 `Thing` 的子类的 `Tile` 。

## 3. Sorter类型、其子类BubbleSorter以及Comparable接口

这里的 `Sorter` 可以排序任意 `Comparable` 的对象，即只要一种对象存在 `Comparable` 接口即可进行排序。而 `Sorter` 本身也作为一种接口，每一种排序算法比如 `BubberSorter` 都需要有 `Sorter` 接口。