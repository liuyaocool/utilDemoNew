assume cs:code

; 逻辑运算 and or
code segment

	start:	mov al,01010011B
			and al,00001111B 	;同1为1

			mov al,00001111B
			or al,00000000B		; 有1为1

			mov ax,4c00H
			int 21h
code ends

end start