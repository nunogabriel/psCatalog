import { Moment } from 'moment';

export interface IOrderDetHst {
    id?: number;
    orderId?: number;
    productId?: number;
    orderQuantity?: number;
    unitPrice?: number;
    createdBy?: string;
    createdDate?: Moment;
    lastModifiedBy?: string;
    lastModifiedDate?: Moment;
    orderDetId?: number;
}

export class OrderDetHst implements IOrderDetHst {
    constructor(
        public id?: number,
        public orderId?: number,
        public productId?: number,
        public orderQuantity?: number,
        public unitPrice?: number,
        public createdBy?: string,
        public createdDate?: Moment,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Moment,
        public orderDetId?: number
    ) {}
}
