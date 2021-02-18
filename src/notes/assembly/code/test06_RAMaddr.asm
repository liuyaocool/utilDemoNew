assume cs:code

; 更灵活定位内存地址

; bx 偏移地址寄存器
; si
; di

code segment

	start:		mov bx,0
				mov si,0
				mov di,16

				mov ax,ds:[si]
				mov bx,ds:[di]

				inc si
				mov ax,ds:[bx+si]
				mov ax,ds:[bx+di]

				mov bx,2
				mov al,ds:[bx+5]

				mov bx,3
				mov si,2
				mov di,6				
				mov al,ds:[bx+2+si]	;逻辑清晰 两层循环可用
				mov al,ds:[bx+3+di]	
				mov al,ds:[bx+di+5]
				mov ds:[bx+3+di],al 

			mov ax,4c00H
			int 21h
code ends

end start