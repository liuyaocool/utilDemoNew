assume cs:code

code segment

			; define word
			dw		1,2,3,4,5,6,7,8 ; 字型数据 地址为cs:0

; -------- 
start:		mov bx,0	; 由于定义了dw(数据) 所以定义代码开始位置
						; 或定以begin标记 dw上边添加jmp begin指令 但bx要改为3
			mov ax,0
			
			mov cx,8
sumNum:		add ax,cs:[bx]
			add bx,2
			loop sumNum

			mov ax,4c00H
			int 21h
code ends

end start				; 定义代码结束