/**
* 处理svg文件的nodejs脚本文件，直接点击run.bat文件即可运行。
*   注意：一定要把待处理的svg文件放在同级目录下的svgs文件夹里。
*
* 这个文件会对svg文件做如下处理：在方法：handlerFileContent()里可看到。
* 1、修改所有的样式类名。就是给其加一个随机字符串后缀，使每个文件的每个类名都唯一。
* 2、去掉很多在visio导出到svg时生成的多余的属性和标签。为了减小svg文件的体积。
*/
    console.log('\n===================开始读取文件===================\n');  
      
    var fs= require('fs');  
    var util = require('util');
    var dirPath = "./svgs";
    var allFilesCount = 0;
    var handledFilesCount = 0;

    function readAllFiles(path){
        allFilesCount = 0;
        handledFilesCount = 0;
        fs.readdir(path, function(err,files){
            if(err){ //如果是有错误的。
                console.log("error:\n"+err);
                return;
            }
            //if correct 
            var filesNum = 0;
            allFilesCount = files.length;
            files.forEach(function(file){
                fs.stat(path + '/' + file, function(err, stat){
                    if(err){
                        console.log("this file error:"+err); return;
                    }
                    if(stat.isDirectory()){ //如果还是文件夹
                        readAllFiles(path+'/'+file);
                    }else{ //如果是文件了。
                        filesNum++;
                        readOneFile(path,file,filesNum);
                        
                    }
                });
            });
        });
    }

    function readOneFile(path,file,filesNum){
        fs.readFile(path+'/'+file,'utf-8',function(err,data){ 
            if(err){  console.log(err);   return; }

            var newData = handlerFileContent(data);
            if(newData.isChanged){
                fs.writeFile(path+'/'+file,newData.data);
                console.log("第"+filesNum+"文件处理完成，文件名："+file);
            }else{ //have not replace any string. means this file not changed.
                console.log("第"+filesNum+"文件没有任何改动，文件名："+file);
            }
            // ===================分割线=============
            handledFilesCount++;
            if(handledFilesCount == allFilesCount){
                console.log('\n===================所有文件处理完成===================');
            }
            
        });
    }


    // 处理文本文件
    function handlerFileContent(data){
        var index = 0;
        var postfix = Math.random().toString(16).split(".")[1];
        var isChanged = false;
        var regTitle = new RegExp('<title>工作表.[0-9]{2,}<\/title>', ["g"]);

        while(index<=100){
            index++;
            // data = data.replace(/\s/g,' '); //去掉所有多余的空格和换行，但好像没什么用。并没减少体积。
            if(data.indexOf("st"+index+" ") != -1){ //修改.stxx
                isChanged = true;
                data = data.replace(new RegExp("st"+index+" ", ["g"]),'st'+index+'_'+postfix+' '); 
            }
            if(data.indexOf('="st'+index+'"') != -1){ //修改="stxx"
                isChanged = true;
                data = data.replace(new RegExp('="st'+index+'"', ["g"]),'="st'+index+'_'+postfix+'"');
            }
            if(data.indexOf('v:groupContext="shape"') != -1){ //去掉所有v:groupContext="shape"
                isChanged = true;
                data = data.replace(new RegExp('v:groupContext="shape"', ["g"]),'');
            }
            if(data.indexOf('v:langID=') != -1){ //去掉所有v:langID="2052"
                isChanged = true;
                data = data.replace(new RegExp('v:langID="[0-9]+"', ["g"]),'');
            }
            if(data.indexOf('v:nameU=') != -1){ //去掉所有v:nameU="Row_x"
                isChanged = true;
                data = data.replace(new RegExp('v:nameU="[a-zA-Z0-9]+_*[a-zA-Z0-9]+"', ["g"]),'');
            }
            if(data.indexOf('id="shape') != -1){ //去掉所有id="shapexx_xx"
                isChanged = true;
                data = data.replace(new RegExp('id="shape[0-9]+-[0-9]+"', ["g"]),'');
            }
            if(data.indexOf('v:mID="') != -1){ //去掉所有v:mID="xxx"
                isChanged = true;
                data = data.replace(new RegExp('v:mID="[0-9]+"', ["g"]),'');
            }
            if(regTitle.test(data)){ //去掉所有<title>工作表.xx</title>xx至少两位以上的,这是为了减小体积，但是又要保留一些有用。
                isChanged = true;
                data = data.replace(regTitle, '');
            }
            

        }
        return {isChanged:isChanged,data:data};
    }
    
    readAllFiles(dirPath);
    
    
      
