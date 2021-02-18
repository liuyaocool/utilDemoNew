assume cs:code

code segment

			dw 		0123H,0456H,0789H,0ABCH,0EDFH,0CBAH,0987H
			
			dw		0,0,0,0,0,0,0,0 ; 初始化数据段 可以当做栈空间
			dw		0,0,0,0,0,0,0,0

; -------- 
start:		mov ax,cs
			mov ss,ax
			mov sp,48 ; 通过dw 设置 计算栈空间

			mov bx,0
			mov cx,8
pushData:	push cs:[bx]
			add bx,2
			loop pushData

			mov bx,0
			mov cx,8
popData:	pop cs:[bx]
			add bx,2
			loop popData


			mov ax,4c00H
			int 21h
code ends

end start				; 定义代码结束