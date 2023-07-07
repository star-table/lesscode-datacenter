## ETL Stage Sample
```
{
    "stage_1595939571740": {
        "id": "stage_1595939571740",
        "type": "filter",
        "posX": 1151,
        "posY": 200,
        "title": "数据筛选",
        "input": [
            "stage_1595939571653"
        ],
        "filter": {
            "rel": "and",
            "cond": [
                {
                    "type": "user",
                    "method": "equal",
                    "value": "123",
                    "field": "_widget_1595939571733"
                }
            ]
        }
    },
    "stage_1595939571653": {
        "id": "stage_1595939571653",
        "type": "group",
        "posX": 899,
        "posY": 200,
        "title": "分组汇总",
        "input": [
            "stage_1595939571578"
        ],
        "dimensions": [
            {
                "name": "_widget_1595939571733",
                "src": {
                    "_input": "_widget_1595939571685"
                },
                "type": "user",
                "title": "a"
            }
        ],
        "metrics": [
            {
                "name": "_widget_1595939571735",
                "src": {
                    "_input": "_widget_1595939571687"
                },
                "type": "user",
                "title": "c",
                "op": "count"
            }
        ]
    },
    "stage_1595939571578": {
        "id": "stage_1595939571578",
        "type": "union",
        "posX": 642,
        "posY": 200,
        "title": "追加合并",
        "input": [
            "stage_1595939571478",
            "stage_1595939571526"
        ],
        "fields": [
            {
                "name": "_widget_1595939571685",
                "title": "a",
                "type": "date",
                "src": {
                    "stage_1595939571478": "_field_1596116341404",
                    "stage_1595939571526": "_field_1596434874095"
                }
            },
            {
                "name": "_widget_1595939571686",
                "title": "b",
                "type": "decimal",
                "src": {
                    "stage_1595939571478": "_field_1596116341406",
                    "stage_1595939571526": "_field_1596434874097"
                }
            },
            {
                "name": "_widget_1595939571687",
                "title": "c",
                "type": "int",
                "src": {
                    "stage_1595939571478": "_field_1596116341407",
                    "stage_1595939571526": "_field_1596434874099"
                }
            },
            {
                "name": "_widget_1595939571688",
                "title": "d",
                "type": "bigint",
                "src": {
                    "stage_1595939571478": "_field_1596116341409",
                    "stage_1595939571526": "_field_1596434874101"
                }
            },
            {
                "name": "_widget_1595939571689",
                "title": "e",
                "type": "varchar",
                "src": {
                    "stage_1595939571478": "_field_1596116341411",
                    "stage_1595939571526": "_field_1596434874103"
                }
            }
        ]
    },
    "stage_1595939571478": {
        "id": "stage_1595939571478",
        "type": "join",
        "posX": 399,
        "posY": 133,
        "title": "横向连接",
        "input": [
            "stage_1595923518030",
            "stage_1595939571525"
        ],
        "join": "inner",
        "relation": [
            {
                "type": "string",
                "src": {
                    "stage_1595923518030": "_field_1596116341404",
                    "stage_1595939571525": "_field_1596434874095"
                }
            }
        ],
        "merge_rel_field": true
    },
    "stage_1595923518030": {
        "id": "stage_1595923518030",
        "type": "input",
		"formId": 1596116341027,
        "posX": 152,
        "posY": 84,
        "title": "first",
        "mainFields": [
            "_field_1596116341404",
            "_field_1596116341406",
            "_field_1596116341407",
            "_field_1596116341409",
            "_field_1596116341411",
			"_field_1596116341413",
			"_field_1596116341415",
			"_field_1596116341416"
        ],
        "subformFields": [
            
        ]
    },
    "stage_1595939571525": {
        "id": "stage_1595939571525",
        "type": "input",
        "posX": 152,
        "posY": 172,
        "title": "second",
		"formId": 1596434873878,
        "mainFields": [
            "_field_1596434874095",
            "_field_1596434874097",
            "_field_1596434874099",
            "_field_1596434874101",
            "_field_1596434874103",
            "_field_1596434874105",
            "_field_1596434874107",
            "_field_1596434874109" 
        ]
    },
	"stage_1595939571526": {
        "id": "stage_1595939571526",
        "type": "input",
        "posX": 152,
        "posY": 172,
        "title": "second",
		"formId": 1596434873878,
        "mainFields": [
            "_field_1596434874095",
            "_field_1596434874097",
            "_field_1596434874099",
            "_field_1596434874101",
            "_field_1596434874103",
            "_field_1596434874105",
            "_field_1596434874107",
            "_field_1596434874109" 
        ]
    },
    "stage_1595923518031": {
        "id": "stage_1595923518031",
        "type": "output",
        "posX": 1282,
        "posY": 349,
        "title": "输出",
        "input": [
            "stage_1595939571740"
        ]
    }
}
```