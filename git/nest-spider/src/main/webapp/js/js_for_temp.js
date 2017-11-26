//add1 & add2 是临时函数，只是为了前端开发时使用，到后端编写时请转换为对应的
//addDynamicField & addStaticField 函数
function add1() {
    $('#dynamicFields').append('<div id="dynamicField1" class="dynamicField" name="example">\n' +
        '                                    <div class="form-group">\n' +
        '                                        <label for="exampleReg">example正则</label>\n' +
        '                                        <input type="text" class="form-control reg" id="exampleReg" name="exampleReg" placeholder="example正则">\n' +
        '                                    </div>\n' +
        '                                    <div class="form-group">\n' +
        '                                        <label for="exampleXPath">exampleXPath</label>\n' +
        '                                        <input type="text" class="form-control xpath" id="exampleXPath" name="exampleXPath" placeholder="exampleXPath">\n' +
        '                                    </div>\n' +
        '                                    <div class="checkbox">\n' +
        '                                        <label>\n' +
        '                                            <input type="checkbox" name="need\' + fieldName + \'" id="needExample">\n' +
        '                                            是否网页必须有example\n' +
        '                                        </label>\n' +
        '                                    </div>\n' +
        '                                    <button class="btn btn-danger" type="button" onclick="$(\'#dynamicField1\').remove();">删除动态字段example</button>\n' +
        '                                </div>');
}

function add2() {
    $('#staticFields').append('<div id="staticField1" class="staticField" name="example">\n' +
        '                                    <div class="form-group">\n' +
        '                                        <label for="example2Reg">example2正则</label>\n' +
        '                                        <input type="text" class="form-control reg" id="example2Reg" name="example2Reg" placeholder="example2正则">\n' +
        '                                    </div>\n' +
        '                                    <div class="form-group">\n' +
        '                                        <label for="example2XPath">example2XPath</label>\n' +
        '                                        <input type="text" class="form-control xpath" id="example2XPath" name="example2XPath" placeholder="exampleXPath">\n' +
        '                                    </div>\n' +
        '                                    <div class="checkbox">\n' +
        '                                        <label>\n' +
        '                                            <input type="checkbox" name="need\' + fieldName + \'" id="needExample2">\n' +
        '                                            是否网页必须有example2\n' +
        '                                        </label>\n' +
        '                                    </div>\n' +
        '                                    <button class="btn btn-danger" type="button" onclick="$(\'#staticField1\').remove();">删除静态字段example2</button>\n' +
        '                                </div>');
}