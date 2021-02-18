assume cs:code

code segment

			mov ax,2000H
			mov ss,ax
			mov sp,0
			add sp,10H
			pop ax
			pop bx
			push ax
			push bx
			pop ax
			pop bx
			
			;----------------------
		
			mov ax,ds:[0]
			mov bl,ds:[0]
			
			mov bx,0
			mov ax,ds:[bx]
			
			add bx,2
			mov ax,ds:[bx]

			add bx,2
			mov ax,ds:[bx]

			;----------------------

			mov bx,10h
			mov al,ds:[bx]

			add bx,1
			mov al,ds:[bx]

			add bx,1
			mov al,ds:[bx]

			;----------------------

			mov ax,2000H
			mov ds,ax
			mov bx,1000H

			mov dl,0beh
			mov ds:[bx],dl	;设置数据

			mov ax,ds:[bx]
			inc bx
			inc bx

			mov ds:[bx],ax
			inc bx 
			inc bx

			mov ds:[bx],ax
			inc bx 
			
			mov ds:[bx],al
			inc bx 

			mov ds:[bx],ax

; -------- 向2000H:1000H 填写0123456789ABCDEF字节型数据 ---------------
			mov ax,2000H
			mov ds,ax
			mov bx,1000H

			mov dl,0

			mov ax,1000H	;测试 jmp setNumber

			mov cx,16
setNumber:	mov ds:[bx],dl	;假设这一行所在地址为000AH 见77行
			inc dl
			inc bx

; 			; jmp 000AH 
; 			jmp setNumber

			loop setNumber 	; 配合 mov cx,16使用 
							; cx不可设置为0 因为会先-1 再cx==0
							; debug中p命令完成loop

			
; -------- 计算123*236 存到ax中 ---------------
			mov ax,0

			mov cx,123
addNum:		add ax,236
			loop addNum

; -------- sum(FFFF:0~FFFF:F)字节型数据的的和 放到dx --------
			mov ax,0FFFFH
			mov ds,ax
			mov bx,0

			mov dx,0	; 此两行为初始化
			mov ax,0	; ah=0 al=读取的字节

			mov cx,16
sumNum:		mov al,ds:[bx]
			add dx,ax
			inc bx
			loop sumNum

; -------- FFFF:0~FFFF:F 复制到 0:200~0:20F中 ------------
			mov bx,0
			mov cx,16
; copyF:		push ds	; 这里没有设置栈段 所以有可能修改其他程序用到的内存
copyF:		mov ax,0FFFFH	; 这里重复进行地址的设置 会消耗CPU资源
			mov ds,ax
			mov dl,ds:[bx]			
			mov ax,20
			mov ds,ax
			mov ds:[bx],dl
			inc bx
			; pop ds
			loop copyF

		; -- 其他版本 ----------------
			mov ax,0FFFFH
			mov ds,ax
			mov ax,20H
			mov es,ax

			mov bx,0
			mov cx,16
copyFB:		mov dl,ds:[bx]
			mov es:[bx],dl
			inc bx
			loop copyFB

			mov bx,0
			mov cx,8	; 比16快了一倍
copyFC:		mov dx,ds:[bx]
			mov es:[bx],dx
			add bx,2
			loop copyFC


			mov bx,0
			mov cx,8
copyFD:		push ds[bx]	;需要设置stack段 才能保证安全
			pop es:[bx]
			add bx,2
			loop copyFD

; -------- 向0:200~0:23F 传送数据 0~63(3FH), 仅7条指令(不包括code ends前两条) --------
			mov ax,20H
			mov es,ax

			mov bx,0
			mov cx,64
seqNum:		mov es:[bx],bl
			inc bx
			loop seqNum

; -------- 

			mov ax,4c00H
			int 21h
code ends

end