import { Moment } from 'moment';

export interface IOrderDet {
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
}

export class OrderDet implements IOrderDet {
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
        public productId?: number
    ) {}
}
