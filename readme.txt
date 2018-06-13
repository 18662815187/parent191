common:通用工具  jar包
console:后台  war包
portal：前台  war
login： 登录 war
dao：jar
interface：jar
cob：order、cart、buyer（user） war
pojo：model   jar
solr：搜索  war 高亮
product：后台商品管理 war
cms：内容管理（评论、晒单、静态化） war
parent191：父类工程 pom


依赖管理pom.xml
pojo中加入common的依赖管理
例：
<dependencies>
<dependency>
<groupId>cn.itcast.babasport</groupId>
<artifaciId>babasport-common</artifactId>
<version>0.0.1-SNAPSHOT</version>
</dependency>
</dependencies>
interface加入pojo
dao加入pojo
console、login、portal加入interface
cms、cob、product、solr加入dao、interface

生命周期
Clean
下面几个在一个生命周期内
Compile 编译
Test	测试
Package 打包
Install 本地仓库
Deploy 发布到私服



parent191 坐标gav（group、artifaciId、version） 版本号 设置不强制依赖
common 坐标ga
pojo 无
interface 无
portal console login  四个service 无