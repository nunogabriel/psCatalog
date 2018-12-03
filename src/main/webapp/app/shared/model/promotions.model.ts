import { Moment } from 'moment';

export interface IPromotions {
    id?: number;
    newProductPrice?: number;
    promotionStartDate?: Moment;
    promotionExpiryDate?: Moment;
    createdBy?: string;
    createdDate?: Moment;
    lastModifiedBy?: string;
    lastModifiedDate?: Moment;
    productsProductName?: string;
    productsId?: number;
}

export class Promotions implements IPromotions {
    constructor(
        public id?: number,
        public newProductPrice?: number,
        public promotionStartDate?: Moment,
        public promotionExpiryDate?: Moment,
        public createdBy?: string,
        public createdDate?: Moment,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Moment,
        public productsProductName?: string,
        public productsId?: number
    ) {}
}
