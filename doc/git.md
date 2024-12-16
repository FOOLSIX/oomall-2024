# Git使用指南
## 用IntelliJ IDEA
### 克隆云端仓库到本地环境
获取仓库地址。
进入仓库详情，单击“克隆/下载”按钮获取HTTPS地址，在Idea中选择Git->Clone将代码克隆到本地

### 创建分支
在Idea中的Project窗口中，选中OOMALL，点击右键，选择Git->New Branch创建分支 xxx可用关联任务编号-姓名缩写代替,要保证服务器上没有同名分支

### 同步master代码
选择Git->Branches可以看到本地的多个分支，选中分支可以checkout分支（把代码切换到某个分支），选择Git->Pull从服务器更新最新代码，选择Git->Merge可以将分支与Master的代码同步

## 用命令行
### 克隆云端仓库到本地环境
获取仓库地址。
进入仓库详情，单击“克隆/下载”按钮获取HTTPS地址
打开cmd。运行`git clone https://codehub.devcloud.cn-north-4.huaweicloud.com/OOMALL00024/OOMALL.git`

进入OOMALL目录
运行 `git status`可以看到当前的状态是
`On branch master
Your branch is up to date with 'origin/master'.

nothing to commit, working tree clean`

### 在本地保存用户名密码
`git config --global credential.helper store`
在第一次使用用户名密码后，之后会保持在本地
在Idea中的Setting->Version Control->Git中选择Use Credential Helper

### 创建分支
在开发前，用`git branch xxx-dev`需要先创建一个本地的开发分支, xxx可用关联任务编号-姓名缩写代替,要保证服务器上没有同名分支
用`git branch`能看到所有分支，*表示的是当前分支

用`git checkout xxx-dev`将当前分支切换到xxx-dev, 就可以在当前分支上写自己的代码

### 同步master代码
在开发过程中git仓库中的master分支会不断加入新的代码，所以每次开始工作前，应该将本地的master更新到服务器的最新代码
用`git checkout master`将分支切换成master
用`git pull`将本地的master分支更新成服务器上最新的代码
用`git checkout xxx-dev`
用``git merge master`将最新的master代码合并到dev中

### 在git服务器上建立新的分支，并将dev分支上传
用`git checkout xxx-dev`切换到dev分支
用`git push origin` 将本地的dev分支提交到服务器创建xxx-dev分支，，
在第一次使用时需要输入用户名和密码，由于前面设定了
`git config --global credential.helper store`
密码会存储在本地，以后就不需要输入了

### 在git服务器上请求合并
在华为codearts项目的代码托管仓库中，选择xxx-dev分支，选择新建合并请求。
源分支是xxx-dev，目标分支是master
关联工作项中需填入代码的必做任务项





