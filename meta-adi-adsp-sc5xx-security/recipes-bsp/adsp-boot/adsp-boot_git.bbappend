
# If building optee support, enable it and include it in stage 2 image
DEPENDS_append_adsp-sc5xx-optee = " trusted-firmware-a"
STAGE_2_SRC_append_adsp-sc5xx-optee = " ${DEPLOY_DIR_IMAGE}/bl31.elf"

# If we'll be signing the output later, call it unsigned for the signing recipe
# to be able to find it
STAGE_2_TARGET_NAME_adsp-sc5xx-signedboot = "stage2-boot-unsigned.ldr"
