; 除法 div
assume cs:code,ds:data,ss:stack

data segment
	db 		3,0,0,0
data ends

stack segment stack
	dw 		0,0,0,0	
	dw 		0,0,0,0	
	dw 		0,0,0,0	
	dw 		0,0,0,0	
stack ends


code segment

	start:	mov bx,stack
			mov ss,bx
			mov sp,32
			
			mov bx,data
			mov ds,bx

			mov ax,16
			; 8位 第一种
			mov bl.3
			div bl 		; al=5(商) 	ah=1(余数)
			; 8位 第二种
			div byte ptr ds:[0]

			
			mov ax,16
			mov dx,0
			; 16位 第一种
			mov bx,3
			div bx
			; 16位 第一种
			div word ptr ds:[0]

			; 问题
			mov ax,0FFFFH
			mov dx,1		; 1FFFFH /1 = 1FFFFH  → dx=1 ax=0FFFFH
			mov bx,1		; dx=余数 ax=商?
			div bx

			mov ax,4c00H
			int 21H
code ends

end start