## ETL Stage Introduce
#### Input
```
{
    "id": "stage_1595939571526",	//stageId
    "type": "input",				//type：input
    "posX": 152,					//x
    "posY": 172,					//y
    "title": "second",				//标题
	"formId": 1596434873878,		//表单id
    "mainFields": [					//字段列表
        "_field_1596434874095",		
        "_field_1596434874097",
        "_field_1596434874099",
        "_field_1596434874101",
        "_field_1596434874103",
        "_field_1596434874105",
        "_field_1596434874107",
        "_field_1596434874109" 
    ]
}
```
#### Join
```
{
    "id": "stage_1595939571478",	//stageId
    "type": "join",					//type: join
    "posX": 399,					//x
    "posY": 133,					//y
    "title": "横向连接",				//标题
    "input": [						//要join的stages
        "stage_1595923518030",	
        "stage_1595939571525"
    ],
    "join": "inner",				//join类型，inner, left, right
    "relation": [					//关联信息
        {
            "type": "string",
            "src": {				//关联的字段
                "stage_1595923518030": "_field_1596116341404",		
                "stage_1595939571525": "_field_1596434874095"
            }
        }
    ],
    "merge_rel_field": true
}
```
#### Union
```
{
    "id": "stage_1595939571578",	//stageId
    "type": "union",				//type: union
    "posX": 642,					//x
    "posY": 200,					//y
    "title": "追加合并",				//标题	
    "input": [						//要合并的stage
        "stage_1595939571478",
        "stage_1595939571526"
    ],
    "fields": [						//合并字段
        {
            "name": "_widget_1595939571685",						//合并后的字段名：前端生成
            "title": "a",											//合并后的标题
            "type": "date",											//字段类型
            "src": {												//合并信息
                "stage_1595939571478": "_field_1596116341404",		
                "stage_1595939571526": "_field_1596434874095"
            }
        }
    ]
}
```
#### Filter
```
{
    "id": "stage_1595939571740",			//stageId
    "type": "filter",						//type: filter
    "posX": 1151,							//x
    "posY": 200,							//y
    "title": "数据筛选",						//标题
    "input": [								//要筛选的stage
        "stage_1595939571653"
    ],
    "filter": {								//条件
        "rel": "and",						//and、or
        "cond": [							//具体条件
            {
                "type": "user",
                "method": "eq",					//eq,ne,gt,gte,lt,lte,range,empty,not_empty,in,nin
                "value": "123",
                "field": "_widget_1595939571733"
            }
        ]
    }
}
```
#### Group
```
{
    "id": "stage_1595939571653",			//stageId
    "type": "group",						//type: group
    "posX": 899,							//x
    "posY": 200,							//y
    "title": "分组汇总",						//标题
    "input": [								//要分组的input
        "stage_1595939571578"
    ],
    "dimensions": [							//group的字段
        {
            "name": "_widget_1595939571733",		//group之后的别名
            "src": {								//group的字段
                "_input": "_widget_1595939571685"
            },
            "type": "user",							
            "title": "a"							//标题
        }
    ],
    "metrics": [									//聚合字段
        {	
            "name": "_widget_1595939571735",		//聚合之后的别名
            "src": {
                "_input": "_widget_1595939571687"	//聚合的字段
            },
            "type": "user",					
            "title": "c",							//标题		
            "op": "count"							//count, sum, avg, min, max
        }
    ]
}
```
#### Output
```
{
    "id": "stage_1595923518031",	//stageId
    "type": "output",				//type: output
    "posX": 1282,					//x
    "posY": 349,					//y
    "title": "输出",					//标题
    "input": [
        "stage_1595939571740"	//指定输出的stage
    ]
}
```