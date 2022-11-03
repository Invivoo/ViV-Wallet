<template>
    <button class="tertiary-button delete-button" @click="requestDeletePayment">
        <span class="sr-only">Supprimer paiement {{ payment.id }}</span>
        <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke-width="1.5"
            stroke="currentColor"
            class="w-6 h-6"
            width="24"
            height="24"
        >
            <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M14.74 9l-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 01-2.244 2.077H8.084a2.25 2.25 0 01-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 00-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 013.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 00-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 00-7.5 0"
            />
        </svg>
    </button>
    <Dialog :open="isOpen" :initial-focus="cancelButtonRef" class="modal-overlay" @close="cancel">
        <DialogPanel class="modal-content">
            <DialogTitle class="modal-title">Supprimer paiement</DialogTitle>
            <DialogDescription class="modal-description"
                >Etes-vous sûr de vouloir supprimer ce paiement ({{ payment.date.toDateString() }} -
                {{ payment.viv }} VIV) ?</DialogDescription
            >
            <div class="buttons-container">
                <button class="primary-red-button" @click="deletePayment">Delete</button>
                <button ref="cancelButtonRef" class="secondary-gray-button" @click="cancel">Cancel</button>
            </div>
        </DialogPanel>
    </Dialog>
</template>

<script lang="ts">
import { Dialog, DialogDescription, DialogPanel, DialogTitle } from "@headlessui/vue";
import { defineComponent, PropType, ref } from "vue";
import { Payment } from "../models/payment";

export default defineComponent({
    name: "DeletePaymentButton",
    components: { Dialog, DialogDescription, DialogPanel, DialogTitle },
    props: {
        payment: {
            required: true,
            type: Object as PropType<Payment>,
        },
    },
    emits: ["paymentDelete"],
    setup(props, { emit }) {
        const cancelButtonRef = ref(undefined);

        const isOpen = ref(false);

        function setIsOpen(value: boolean) {
            isOpen.value = value;
        }

        function requestDeletePayment() {
            setIsOpen(true);
        }

        function cancel() {
            setIsOpen(false);
        }

        function deletePayment() {
            setIsOpen(false);
            emit("paymentDelete", props.payment.id);
        }
        return {
            cancelButtonRef,
            isOpen,
            cancel,
            deletePayment,
            requestDeletePayment,
        };
    },
});
</script>

<style lang="scss">
.delete-button {
    color: $gray-700;
    &:hover {
        color: $gray-900;
    }
}

.modal-overlay {
    z-index: 1000;
    isolation: isolate;
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: hsl(220deg 5% 40% / 0.6);
    display: grid;
    place-content: center;
}

.modal-content {
    min-width: 40vw;
    background: $white;
    box-shadow: 0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1);
    border-radius: $rounded-md;
    padding: $m-6 $m-6;
    position: relative;
    font-family: "Open Sans", sans-serif;
}

.modal-title {
    font-size: $text-2xl;
    font-weight: 500;
    margin: 0;
    margin-bottom: $m-4;
    color: $gray-900;
}

.modal-description {
    color: $gray-700;
    font-weight: 400;
    font-size: $text-base;
}

.buttons-container {
    margin-top: $m-4;
    display: flex;
    flex-direction: row;
    justify-content: flex-end;
    gap: $m-2;
}
</style>
