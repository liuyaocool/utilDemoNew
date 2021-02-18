; 伪指令 告诉编译器怎么翻译
assume cs:code,ds:data,ss:stack


; 数据段和栈段 无论设置多少个字节, 最终会被编译器补成 16的倍数 个字节
; 即 16 32 48 64 ...
data segment
			dw 		0123H,0456H,0789H,0ABCH,0EDFH,0CBAH,0987H
data ends

stack segment stack
			dw		0,0,0,0,0,0,0,0
			dw		0,0,0,0,0,0,0,0
stack ends

code segment

			; 数据段
			; dw 		0123H,0456H,0789H,0ABCH,0EDFH,0CBAH,0987H
			; 初始化栈段 数据段模式 
			; 但会占用IP寄存器的范围(0~FFFFH), 间接减少指令的数量
			; dw		0,0,0,0,0,0,0,0
			; dw		0,0,0,0,0,0,0,0

; -------- 
start:		mov ax,stack; start 将程序入口地址 记录在exe文件的描述信息中
			mov ss,ax
			mov sp,32 ; 设置栈段 因为栈起始位置为最后一个字节 所以需要设置

			mov ax,data
			mov ds,ax ; 设置数据段 是否必要?

			mov bx,0
			mov cx,8
pushData:	push ds:[bx]
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