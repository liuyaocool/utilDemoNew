; 寻址方式在结构化数据访问中的应用
; 	结构化数据在访问的时候特别方便
;	数据被结构化了 table段
;		偏移的思想可以得到某一年的的所有信息
;		表格 结构化
assume cs:code,ds:data,ss:stack

; 计算每年的人均收入 按照table段格式放入??位置
data segment
			; 7个年份 
	db		'1975','1976','1977','1978','1979','1980','1981'
			; 每年总收入
	dd		16,2390,16000,140417,1183000,3753000,5937000
			; 每年员工数
	dw		3,28,138,1001,5635,14430,17800
data ends

table segment
	db		7	dup ('year summ ne ?? ')	; 7个16字节
table ends

stack segment stack
	db		128	dup (0)
stack ends

code segment

	start:	mov bx,stack
			mov ss,bx
			mov sp,128
			
			mov bx,data
			mov ds,bx

			mov bx,table
			mov es,bx

			; bx与si每次都是+4  ------------------
			; 则si 可以替换成 bx+4*7+n的形式  -----------------
			mov bx,0		; data段年份 起始偏移
			mov si,4*7		; data段总收入 起始偏移
			mov di,4*7+4*7	; data段员工数 起始偏移
			mov bp,0		; table段偏移 和 
			mov cx,7

avgSv:		mov ax,ds:[bx]
			mov es:[bp],ax
			mov ax,ds:[bx+2]
			mov es:[bp+2],ax		; 年份
			; 年份第二种
			push ds:[bx+0]
			pop es:[bp+0]
			push ds:[bx+2]
			pop es:[bp+2]

			mov ax,ds:[si+0]
			mov dx,ds:[si+2]

			mov es:[bp+5],ax
			mov es:[bp+7],dx		; 总收入

			div word ptr ds:[di]
			mov es:[bp+13],ax		; 平均工资 商

			mov ax,ds:[di]
			mov es:[bp+10],ax		; 员工数
			; 员工数第二种
			push ds:[di]
			pop es:[bp+10]

			add bx,4
			add si,4
			add di,2
			add bp,16
			loop avgSv

			mov ax,4c00H
			int 21H
code ends

end start