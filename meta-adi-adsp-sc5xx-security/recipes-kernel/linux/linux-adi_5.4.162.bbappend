
do_assemble_fitimage_append_adsp-sc5xx-signedboot() {
	uboot-mkimage -D "${UBOOT_MKIMAGE_DTCOPTS}" -F -k ${UBOOT_SIGN_KEYDIR} \
		-r arch/arm64/boot/fitImage
}

