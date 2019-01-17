import { Moment } from 'moment';

export const enum ProductTypeEnum {
    PRODUTO = 'PRODUTO',
    SERVICO = 'SERVICO'
}

export interface ICart {
    id?: number;
    orderQuantity?: number;
    unitPrice?: number;
    createdBy?: string;
    createdDate?: Moment;
    lastModifiedBy?: string;
    lastModifiedDate?: Moment;
    orderOrderReference?: string;
    orderId?: number;
    productProductName?: string;
    productId?: number;
    productDescription?: string;
    productType?: ProductTypeEnum;
    productImgContentType?: string;
    productImg?: any;
}

export class Cart implements ICart {
    constructor(
        public id?: number,
        public orderQuantity?: number,
        public unitPrice?: number,
        public createdBy?: string,
        public createdDate?: Moment,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Moment,
        public orderOrderReference?: string,
        public orderId?: number,
        public productProductName?: string,
        public productId?: number,
        public productDescription?: string,
        public productType?: ProductTypeEnum,
        public productImgContentType?: string,
        public productImg?: any
    ) {}
}
